package com.tweaty.reservation.presentation.request;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ReservationRequestDto {

	private UUID storeId;
	private int guestCount;

}
