package com.tweaty.reservation.presentation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.reservation.application.ReservationService;
import com.tweaty.reservation.application.dto.ReservationResponseDto;
import com.tweaty.reservation.presentation.request.ReservationRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping
	public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDto requestDto) {
		reservationService.createReservation(requestDto);

		return ResponseEntity.status(HttpStatus.CREATED).body("예약이 생성되었습니다.");
	}

	@GetMapping("/{reservationId}")
	public ResponseEntity<?> getReservationById(@PathVariable UUID reservationId) {
		ReservationResponseDto reservation = reservationService.getReservationById(reservationId);
		return ResponseEntity.status(HttpStatus.OK).body(reservation);
	}

}
