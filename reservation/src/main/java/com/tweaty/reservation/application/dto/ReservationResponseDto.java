package com.tweaty.reservation.application.dto;

import java.util.List;

import com.tweaty.reservation.domain.model.Reservation;
import com.tweaty.reservation.domain.model.ReservationStatus;

import lombok.Getter;

@Getter
public class ReservationResponseDto {

	private String storeName;
	private int guestCount;
	private ReservationStatus status;
	private String reservationTime;
	private String reservationDate;

	public static ReservationResponseDto from(Reservation reservation) {
		ReservationResponseDto responseDto = new ReservationResponseDto();
		responseDto.storeName = null;
		responseDto.guestCount = reservation.getGuestCount();
		responseDto.status = reservation.getStatus();
		responseDto.reservationTime = reservation.getCreatedAt().toString();
		responseDto.reservationDate = reservation.getCreatedAt().toString();

		return responseDto;
	}

	public static List<ReservationResponseDto> froms(List<Reservation> reservations) {
		return reservations.stream().map(ReservationResponseDto::from).toList();
	}

	public void updateStoreName(String storeName) {
		this.storeName = storeName;
	}
}
