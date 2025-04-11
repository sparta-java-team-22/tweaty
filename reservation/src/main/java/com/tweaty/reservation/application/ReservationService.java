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

	public List<ReservationResponseDto> getReservation() {
		List<Reservation> reservationList = reservationRepository.findAll();
		return ReservationResponseDto.from(reservationList);
	}
}
