package com.tweaty.reservation.presentation.request;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ReservationRequestDto {
	private UUID storeId;
	private int tableCount;
	private int reservationTableCount;
	private String reservationTime;
	private String reservationDate;
	private int currentCapacity;
	private int tableTwo;
	private int tableFour;

}
