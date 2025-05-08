package com.tweaty.reservation.presentation.request;

import java.util.UUID;

import com.tweaty.reservation.domain.model.MethodType;

import lombok.Getter;

@Getter
public class ReservationRequestDto {

	private UUID storeId;
	private UUID reservationScheduleId;
	private UUID couponId;
	private int guestCount;
	private MethodType method;

}
