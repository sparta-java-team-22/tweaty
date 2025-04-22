package com.tweaty.reservation.application;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.reservation.application.dto.ReservationResponseDto;
import com.tweaty.reservation.application.dto.StoreResponseDto;
import com.tweaty.reservation.domain.model.Reservation;
import com.tweaty.reservation.domain.model.ReservationSchedule;
import com.tweaty.reservation.domain.model.ReservationStatus;
import com.tweaty.reservation.domain.repository.ReservationRepository;
import com.tweaty.reservation.domain.repository.ReservationScheduleRepository;
import com.tweaty.reservation.presentation.client.PaymentClient;
import com.tweaty.reservation.presentation.client.StoreClient;
import com.tweaty.reservation.presentation.request.PaymentRequestDto;
import com.tweaty.reservation.presentation.request.ReservationRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationScheduleRepository reservationScheduleRepository;
	private final StoreClient storeClient;
	private final PaymentClient paymentClient;

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

		ResponseEntity<?> responseEntity = paymentClient.createKafkaPayment(paymentRequestDto, reservation.getId());

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			reservation.updateStatus(ReservationStatus.COMPLETED);
		} else {
			reservation.updateStatus(ReservationStatus.FAILED);
			throw new IllegalArgumentException("결제에 실패했습니다.");
		}

	}

	public ReservationResponseDto getReservationById(UUID reservationId) {
		Reservation reservation = reservationRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

		return ReservationResponseDto.from(reservation);
	}

	public List<ReservationResponseDto> getAllReservations(UUID userId) {
		List<Reservation> reservations = reservationRepository.findByUserId(userId);

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

		reservationSchedule.updateDeleteTakenCount(reservation.getGuestCount());
		reservation.softDelete();

	}
}
