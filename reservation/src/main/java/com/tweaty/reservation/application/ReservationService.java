package com.tweaty.reservation.application;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.reservation.application.dto.ReservationResponseDto;
import com.tweaty.reservation.application.dto.StoreResponseDto;
import com.tweaty.reservation.domain.model.Reservation;
import com.tweaty.reservation.domain.model.ReservationSchedule;
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
	private final ReservationCacheService cacheService;

	private static final long CACHE_TTL = 10; // 10 minutes

	@Transactional
	public void createReservation(ReservationRequestDto requestDto, UUID userId) {
		ReservationSchedule schedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(
				requestDto.getReservationScheduleId())
			.orElseThrow(() -> new IllegalArgumentException("예약 일정을 찾을 수 없습니다."));
		StoreResponseDto store = storeClient.getStore(requestDto.getStoreId());

		if (store == null || requestDto.getGuestCount() <= 0) {
			throw new IllegalArgumentException("유효하지 않은 요청입니다.");
		}

		schedule.updateTakenCount(requestDto.getGuestCount());
		PaymentRequestDto paymentRequest = PaymentRequestDto.builder()
			.originalAmount(store.getReservationAmount())
			.couponId(requestDto.getCouponId())
			.method(requestDto.getMethod())
			.build();

		Reservation reservation = reservationRepository.save(new Reservation(requestDto, userId));
		ResponseEntity<?> paymentResponse = paymentClient.createKafkaPayment(userId, paymentRequest,
			reservation.getId());

		if (paymentResponse.getStatusCode().is2xxSuccessful()) {
			cacheService.cacheReservation(reservation.getId().toString(), reservation, CACHE_TTL);
		}
	}

	public ReservationResponseDto getReservationById(UUID reservationId) {
		String cacheKey = reservationId.toString();
		Reservation cachedReservation = (Reservation)cacheService.getFromCache(cacheKey);

		if (cachedReservation != null) {
			return new ReservationResponseDto(cachedReservation);
		}

		Reservation reservation = reservationRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		cacheService.cacheReservation(cacheKey, reservation, CACHE_TTL);

		return new ReservationResponseDto(reservation);
	}
}