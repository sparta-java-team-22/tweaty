package com.tweaty.reservation.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/v1/reservation/plan")
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping
	public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDto requestDto) {
		reservationService.createReservation(requestDto);

		return ResponseEntity.status(HttpStatus.CREATED).body("예약 일정이 생성되었습니다.");
	}

	@GetMapping
	public ResponseEntity<?> getReservation() {
		List<?> reservationList = reservationService.getReservation();

		return ResponseEntity.status(HttpStatus.OK).body(reservationList);
	}

	@GetMapping("/{reservationId}")
	public ResponseEntity<?> getReservationById(@PathVariable Long reservationId) {
		ReservationResponseDto reservation = reservationService.getReservationById(reservationId);
		return ResponseEntity.status(HttpStatus.OK).body(reservation);
	}

	@PatchMapping("/{reservationId}")
	public ResponseEntity<?> updateReservation(@PathVariable Long reservationId,
		@RequestBody ReservationRequestDto requestDto) {
		reservationService.updateReservation(reservationId, requestDto);
		return ResponseEntity.status(HttpStatus.OK).body("예약 일정이 수정되었습니다.");
	}

	@DeleteMapping("/{reservationId}")
	public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId) {
		reservationService.deleteReservation(reservationId);
		return ResponseEntity.status(HttpStatus.OK).body("예약 일정이 삭제되었습니다.");
	}

}
