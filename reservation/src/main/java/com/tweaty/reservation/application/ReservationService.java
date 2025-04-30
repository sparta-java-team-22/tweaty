package com.tweaty.reservation.application;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.reservation.application.dto.PaymentIdDto;
import com.tweaty.reservation.application.dto.ReservationResponseDto;
import com.tweaty.reservation.application.dto.StoreResponseDto;
import com.tweaty.reservation.domain.model.Reservation;
import com.tweaty.reservation.domain.model.ReservationSchedule;
import com.tweaty.reservation.domain.model.ReservationStatus;
import com.tweaty.reservation.domain.repository.ReservationRepository;
import com.tweaty.reservation.domain.repository.ReservationScheduleRepository;
import com.tweaty.reservation.presentation.client.NotificationClient;
import com.tweaty.reservation.presentation.client.PaymentClient;
import com.tweaty.reservation.presentation.client.StoreClient;
import com.tweaty.reservation.presentation.request.NotificationRequestDto;
import com.tweaty.reservation.presentation.request.PaymentRequestDto;
import com.tweaty.reservation.presentation.request.RefundRequestDto;
import com.tweaty.reservation.presentation.request.ReservationRequestDto;

import domain.NotiChannel;
import domain.NotiType;
import domain.TargetType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationScheduleRepository reservationScheduleRepository;
	private final StoreClient storeClient;
	private final PaymentClient paymentClient;
	private final NotificationClient notificationClient;
	private final ReservationCacheService cacheService; // Redis Cache Service 주입

	private static final long CACHE_TTL = 10; // 캐시 TTL 설정 (10분)

	@Transactional
	public void createReservation(ReservationRequestDto requestDto, UUID userId) {
		ReservationSchedule reservationSchedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(
				requestDto.getReservationScheduleId())
			.orElseThrow(() -> new IllegalArgumentException("예약 일정을 찾을 수 없습니다."));
		StoreResponseDto store = storeClient.getStore(requestDto.getStoreId());
		if (store == null) {
			throw new IllegalArgumentException("가게를 찾을 수 없습니다.");
		}

		if (requestDto.getGuestCount() <= 0) {
			throw new IllegalArgumentException("손님 수는 1명 이상이어야 합니다.");
		}
		if (requestDto.getStoreId() == null) {
			throw new IllegalArgumentException("가게 ID는 필수입니다.");
		}
		if (requestDto.getReservationScheduleId() == null) {
			throw new IllegalArgumentException("예약 일정 ID는 필수입니다.");
		}
		if (requestDto.getGuestCount() > 10) {
			throw new IllegalArgumentException("손님 수는 최대 10명까지 가능합니다.");
		}
		if (reservationSchedule.getMaxGuestCount() < requestDto.getGuestCount()) {
			throw new IllegalArgumentException("예약 일정의 최대 손님 수를 초과했습니다.");
		}
		reservationSchedule.updateTakenCount(requestDto.getGuestCount());

		PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
			.originalAmount(store.getReservationAmount())
			.couponId(requestDto.getCouponId())
			.method(requestDto.getMethod())
			.build();

		Reservation reservation = reservationRepository.save(new Reservation(requestDto, userId));

		ResponseEntity<?> responseEntity = paymentClient.createKafkaPayment(userId, paymentRequestDto,
			reservation.getId());

		PaymentIdDto paymentIdDto = (PaymentIdDto)responseEntity.getBody();

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			reservation.updateStatus(ReservationStatus.COMPLETED);
			reservation.updatePayment(Objects.requireNonNull(paymentIdDto).getPaymentId(), requestDto.getCouponId());

			// Redis 캐시 저장
			cacheService.cacheReservation("reservation:" + reservation.getId(), reservation, CACHE_TTL);

			Set<NotiChannel> channels = new HashSet<>();
			channels.add(NotiChannel.WEB);
			channels.add(NotiChannel.EMAIL);
			NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
				.receiverId(store.getUserId())
				.targetType(TargetType.RESERVATION)
				.notiChannel(channels)
				.notiType(NotiType.CREATED)
				.reason(null)
				.targetId(userId)
				.count(requestDto.getGuestCount())
				.reservationDateTime(LocalDateTime.parse(
					reservationSchedule.getReservationTime() + " " + reservationSchedule.getReservationDate()))
				.build();
			notificationClient.createReservationNotification(notificationRequestDto);
		} else {
			reservation.updateStatus(ReservationStatus.FAILED);
			throw new IllegalArgumentException("결제에 실패했습니다.");
		}
	}

	public ReservationResponseDto getReservationById(UUID reservationId) {
		String cacheKey = "reservation:" + reservationId;

		// Redis 캐시에서 조회
		Reservation cachedReservation = (Reservation)cacheService.getFromCache(cacheKey);
		if (cachedReservation != null) {
			return ReservationResponseDto.from(cachedReservation);
		}

		// 캐시 미스 시 데이터베이스 조회
		Reservation reservation = reservationRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

		// Redis 캐시에 저장
		cacheService.cacheReservation(cacheKey, reservation, CACHE_TTL);

		ReservationResponseDto responseDto = ReservationResponseDto.from(reservation);
		StoreResponseDto store = storeClient.getStore(reservation.getStoreId());
		if (store == null) {
			throw new IllegalArgumentException("가게를 찾을 수 없습니다.");
		}
		responseDto.updateStoreName(store.getName());

		return responseDto;
	}

	public List<ReservationResponseDto> getAllReservations(UUID userId) {
		// Redis 캐시 키 설정
		String cacheKey = "reservations:user:" + userId;

		// Redis 캐시에서 조회
		@SuppressWarnings("unchecked")
		List<Reservation> cachedReservations = (List<Reservation>)cacheService.getFromCache(cacheKey);
		if (cachedReservations != null) {
			return ReservationResponseDto.froms(cachedReservations);
		}

		// 캐시 미스 시 데이터베이스 조회
		List<Reservation> reservations = reservationRepository.findByUserId(userId);

		// Redis 캐시에 저장
		cacheService.cacheReservation(cacheKey, reservations, CACHE_TTL);

		return ReservationResponseDto.froms(reservations);
	}

	@Transactional
	public void updateReservation(UUID reservationId, ReservationRequestDto requestDto, UUID userId, String role) {
		Reservation reservation = reservationRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		ReservationSchedule reservationSchedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(
			requestDto.getReservationScheduleId()).orElseThrow(() -> new IllegalArgumentException("예약 일정을 찾을 수 없습니다."));

		if (requestDto.getGuestCount() <= 0) {
			throw new IllegalArgumentException("손님 수는 1명 이상이어야 합니다.");
		}
		if (requestDto.getStoreId() == null) {
			throw new IllegalArgumentException("가게 ID는 필수입니다.");
		}
		if (requestDto.getReservationScheduleId() == null) {
			throw new IllegalArgumentException("예약 일정 ID는 필수입니다.");
		}
		if (requestDto.getGuestCount() > 10) {
			throw new IllegalArgumentException("손님 수는 최대 10명까지 가능합니다.");
		}
		if (reservation.getUserId() != userId || role.equals("ADMIN")) {
			throw new IllegalArgumentException("예약을 수정할 권한이 없습니다.");
		}

		reservationSchedule.updateTakenCount(requestDto.getGuestCount(), reservation.getGuestCount());
		reservation.update(requestDto);

		reservationSchedule.updateDeleteTakenCount(reservation.getGuestCount());
		StoreResponseDto store = storeClient.getStore(reservation.getStoreId());
		Set<NotiChannel> channels = new HashSet<>();
		channels.add(NotiChannel.WEB);
		channels.add(NotiChannel.EMAIL);
		NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
			.receiverId(store.getUserId())
			.targetType(TargetType.RESERVATION)
			.notiChannel(channels)
			.notiType(NotiType.MODIFIED)
			.reason(null)
			.targetId(userId)
			.count(reservation.getGuestCount())
			.reservationDateTime(LocalDateTime.parse(
				reservationSchedule.getReservationTime() + " " + reservationSchedule.getReservationDate()))
			.build();
		notificationClient.createReservationNotification(notificationRequestDto);
		reservationRepository.save(reservation);
	}

	@Transactional
	public void deleteReservation(UUID reservationId, UUID userId, String role) {
		Reservation reservation = reservationRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

		ReservationSchedule reservationSchedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(
				reservation.getReservationScheduleId())
			.orElseThrow(() -> new IllegalArgumentException("예약 일정을 찾을 수 없습니다."));

		if (!reservation.getUserId().equals(userId) || !role.equals("ADMIN")) {
			throw new IllegalArgumentException("예약을 취소할 권한이 없습니다.");
		}

		RefundRequestDto requestDto = RefundRequestDto.builder()
			.reason("예약 취소")
			.couponId(reservation.getCouponId())
			.build();

		paymentClient.createRefund(userId, role, requestDto, reservation.getPaymentId());
		reservationSchedule.updateDeleteTakenCount(reservation.getGuestCount());
		StoreResponseDto store = storeClient.getStore(reservation.getStoreId());
		Set<NotiChannel> channels = new HashSet<>();
		channels.add(NotiChannel.WEB);
		channels.add(NotiChannel.EMAIL);
		NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
			.receiverId(store.getUserId())
			.targetType(TargetType.RESERVATION)
			.notiChannel(channels)
			.notiType(NotiType.CANCELLED)
			.reason(null)
			.targetId(userId)
			.count(reservation.getGuestCount())
			.reservationDateTime(LocalDateTime.parse(
				reservationSchedule.getReservationTime() + " " + reservationSchedule.getReservationDate()))
			.build();
		notificationClient.createReservationNotification(notificationRequestDto);
		reservation.softDelete();

	}
}