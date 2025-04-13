package com.tweaty.reservation.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tweaty.reservation.application.dto.ReservationResponseDto;
import com.tweaty.reservation.domain.model.Reservation;
import com.tweaty.reservation.domain.repository.ReservationRepository;
import com.tweaty.reservation.presentation.request.ReservationRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;

	public void createReservation(ReservationRequestDto requestDto) {
		reservationRepository.save(new Reservation(requestDto));
	}

	public List<?> getReservation() {
		List<Reservation> reservationList = reservationRepository.findAll();
		if (reservationList.isEmpty()) {
			return List.of("예약 일정이 없습니다.");
		}
		return ReservationResponseDto.fromList(reservationList);
	}

	public ReservationResponseDto getReservationById(Long reservationId) {
		Reservation reservation = reservationRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		return ReservationResponseDto.from(reservation);
	}
}
