package com.tweaty.reservation.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.reservation.application.dto.ReservationResponseDto;
import com.tweaty.reservation.domain.model.Reservation;
import com.tweaty.reservation.domain.model.ReservationSchedule;
import com.tweaty.reservation.domain.repository.ReservationRepository;
import com.tweaty.reservation.domain.repository.ReservationScheduleRepository;
import com.tweaty.reservation.presentation.request.ReservationRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationScheduleRepository reservationScheduleRepository;

	@Transactional
	public void createReservation(ReservationRequestDto requestDto, UUID userId) {
		ReservationSchedule reservationSchedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(
				requestDto.getReservationScheduleId())
			.orElseThrow(() -> new IllegalArgumentException("예약 일정을 찾을 수 없습니다."));

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

		reservationRepository.save(new Reservation(requestDto, userId));
	}

	public ReservationResponseDto getReservationById(UUID reservationId) {
		Reservation reservation = reservationRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

		return ReservationResponseDto.from(reservation);
	}
}
