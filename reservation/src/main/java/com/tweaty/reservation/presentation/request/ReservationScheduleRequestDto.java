package com.tweaty.reservation.presentation.request;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ReservationScheduleRequestDto {
	private UUID storeId;
	private String reservationTime;
	private String reservationDate;
	private int currentCapacity;
	private int tableTwo;
	private int tableFour;

}
