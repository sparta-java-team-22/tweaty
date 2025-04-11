package com.tweaty.reservation.application.dto;

import java.util.List;
import java.util.UUID;

import com.tweaty.reservation.domain.model.Reservation;

import lombok.Getter;

@Getter
public class ReservationResponseDto {
	private UUID id;
	private UUID storeId;
	private int tableCount;
	private int reservationTableCount;
	private String reservationTime;
	private String reservationDate;
	private int currentCapacity;
	private int tableTwo;
	private int tableFour;

	public static List<ReservationResponseDto> from(List<Reservation> reservationList) {
		return reservationList.stream().map(reservation -> {
			ReservationResponseDto reservationResponseDto = new ReservationResponseDto();
			reservationResponseDto.id = reservation.getId();
			reservationResponseDto.storeId = reservation.getStoreId();
			reservationResponseDto.tableCount = reservation.getTableCount();
			reservationResponseDto.reservationTableCount = reservation.getReservationTableCount();
			reservationResponseDto.reservationTime = reservation.getReservationTime();
			reservationResponseDto.reservationDate = reservation.getReservationDate();
			reservationResponseDto.currentCapacity = reservation.getCurrentCapacity();
			reservationResponseDto.tableTwo = reservation.getTableTwo();
			reservationResponseDto.tableFour = reservation.getTableFour();
			return reservationResponseDto;
		}).toList();
	}
}