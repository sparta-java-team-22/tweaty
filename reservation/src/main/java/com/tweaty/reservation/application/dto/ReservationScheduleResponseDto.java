package com.tweaty.reservation.application.dto;

import java.util.List;
import java.util.UUID;

import com.tweaty.reservation.domain.model.ReservationSchedule;

import lombok.Getter;

@Getter
public class ReservationScheduleResponseDto {
	private UUID id;
	private UUID storeId;
	private int reservationTableTwoCount;
	private int reservationTableFourCount;
	private String reservationTime;
	private String reservationDate;
	private int currentCapacity;
	private int tableTwo;
	private int tableFour;

	public static List<ReservationScheduleResponseDto> fromList(List<ReservationSchedule> reservationScheduleList) {
		return reservationScheduleList.stream().map(reservationSchedule -> {
			ReservationScheduleResponseDto reservationScheduleResponseDto = new ReservationScheduleResponseDto();
			reservationScheduleResponseDto.id = reservationSchedule.getId();
			reservationScheduleResponseDto.storeId = reservationSchedule.getStoreId();
			reservationScheduleResponseDto.reservationTableTwoCount = reservationSchedule.getReservationTableTwoCount();
			reservationScheduleResponseDto.reservationTableFourCount = reservationSchedule.getReservationTableFourCount();
			reservationScheduleResponseDto.reservationTime = reservationSchedule.getReservationTime();
			reservationScheduleResponseDto.reservationDate = reservationSchedule.getReservationDate();
			reservationScheduleResponseDto.currentCapacity = reservationSchedule.getCurrentCapacity();
			reservationScheduleResponseDto.tableTwo = reservationSchedule.getTableTwo();
			reservationScheduleResponseDto.tableFour = reservationSchedule.getTableFour();
			return reservationScheduleResponseDto;
		}).toList();
	}

	public static ReservationScheduleResponseDto from(ReservationSchedule reservationSchedule) {
		ReservationScheduleResponseDto reservationScheduleResponseDto = new ReservationScheduleResponseDto();
		reservationScheduleResponseDto.id = reservationSchedule.getId();
		reservationScheduleResponseDto.storeId = reservationSchedule.getStoreId();
		reservationScheduleResponseDto.reservationTableTwoCount = reservationSchedule.getReservationTableTwoCount();
		reservationScheduleResponseDto.reservationTableFourCount = reservationSchedule.getReservationTableFourCount();
		reservationScheduleResponseDto.reservationTime = reservationSchedule.getReservationTime();
		reservationScheduleResponseDto.reservationDate = reservationSchedule.getReservationDate();
		reservationScheduleResponseDto.currentCapacity = reservationSchedule.getCurrentCapacity();
		reservationScheduleResponseDto.tableTwo = reservationSchedule.getTableTwo();
		reservationScheduleResponseDto.tableFour = reservationSchedule.getTableFour();
		return reservationScheduleResponseDto;
	}
}