package com.tweaty.reservation.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.reservation.application.dto.ReservationScheduleResponseDto;
import com.tweaty.reservation.domain.model.ReservationSchedule;
import com.tweaty.reservation.domain.repository.ReservationScheduleRepository;
import com.tweaty.reservation.presentation.request.ReservationScheduleRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationScheduleRepository reservationScheduleRepository;

	public void createReservation(ReservationScheduleRequestDto requestDto) {
		reservationScheduleRepository.save(new ReservationSchedule(requestDto));
	}

	@Transactional(readOnly = true)
	public List<ReservationScheduleResponseDto> getReservation() {
		List<ReservationSchedule> reservationScheduleList = reservationScheduleRepository.findAll();
		if (reservationScheduleList.isEmpty()) {
			throw new IllegalArgumentException("예약 일정이 없습니다.");
		}
		return ReservationScheduleResponseDto.fromList(reservationScheduleList);
	}

	@Transactional(readOnly = true)
	public ReservationScheduleResponseDto getReservationById(UUID reservationId) {
		ReservationSchedule reservationSchedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		return ReservationScheduleResponseDto.from(reservationSchedule);
	}

	public void updateReservation(UUID reservationId, ReservationScheduleRequestDto requestDto) {
		ReservationSchedule reservationSchedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		reservationSchedule.update(requestDto);
		reservationScheduleRepository.save(reservationSchedule);
	}

	public void deleteReservation(UUID reservationId) {
		ReservationSchedule reservationSchedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		reservationSchedule.softDelete();
		reservationScheduleRepository.save(reservationSchedule);
	}
}
