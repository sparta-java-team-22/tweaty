package com.tweaty.reservation.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional(readOnly = true)
	public List<ReservationResponseDto> getReservation() {
		List<Reservation> reservationList = reservationRepository.findAll();
		if (reservationList.isEmpty()) {
			throw new IllegalArgumentException("예약 일정이 없습니다.");
		}
		return ReservationResponseDto.fromList(reservationList);
	}

	@Transactional(readOnly = true)
	public ReservationResponseDto getReservationById(UUID reservationId) {
		Reservation reservation = reservationRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		return ReservationResponseDto.from(reservation);
	}

	public void updateReservation(UUID reservationId, ReservationRequestDto requestDto) {
		Reservation reservation = reservationRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		reservation.update(requestDto);
		reservationRepository.save(reservation);
	}

	public void deleteReservation(UUID reservationId) {
		Reservation reservation = reservationRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		reservation.softDelete();
		reservationRepository.save(reservation);
	}
}
