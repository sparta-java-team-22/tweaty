package com.tweaty.reservation.application.dto;

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
		responseDto.storeName = reservation.getStoreId().toString();
		responseDto.guestCount = reservation.getGuestCount();
		responseDto.status = reservation.getStatus();
		responseDto.reservationTime = reservation.getCreatedAt().toString();
		responseDto.reservationDate = reservation.getCreatedAt().toString();

		return responseDto;
	}
}
