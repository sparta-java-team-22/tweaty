package com.tweaty.reservation.application.dto;

import com.tweaty.reservation.domain.model.ReservationStatus;

import lombok.Getter;

@Getter
public class ReservationResponseDto {

	private String storeName;
	private int guestCount;
	private ReservationStatus status;
	private String reservationTime;
	private String reservationDate;

}
