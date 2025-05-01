package com.tweaty.reservation.application.dto;

import java.util.UUID;

import com.tweaty.reservation.domain.model.StoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StoreResponseDto {

	private UUID id;
	private UUID userId;
	private String name;
	private String address;
	private String phoneNumber;
	private String description;
	private String openTime;
	private String closedTime;

	private StoreType status;
	private boolean isReservation;
	private boolean isWaiting;

	private int reservationAmount;

	private String imgUrl;

}


