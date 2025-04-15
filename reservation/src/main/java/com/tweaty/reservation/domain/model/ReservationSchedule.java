package com.tweaty.reservation.domain.model;

import java.util.UUID;

import com.tweaty.reservation.presentation.request.ReservationScheduleRequestDto;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_reservation")
public class ReservationSchedule extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "store_id", nullable = false)
	private UUID storeId;

	@Column(name = "table_count", nullable = false)
	private int tableCount;

	@Column(name = "reservation_table_count", nullable = false)
	private int reservationTableCount;

	@Column(name = "reservation_time", nullable = false)
	private String reservationTime;

	@Column(name = "reservation_date", nullable = false)
	private String reservationDate;

	@Column(name = "current_capacity", nullable = false)
	private int currentCapacity;

	@Column(name = "table_two", nullable = false)
	private int tableTwo;

	@Column(name = "table_four", nullable = false)
	private int tableFour;

	public ReservationSchedule(ReservationScheduleRequestDto reservationScheduleRequestDto) {
		this.storeId = reservationScheduleRequestDto.getStoreId();
		this.tableCount = reservationScheduleRequestDto.getTableCount();
		this.reservationTableCount = reservationScheduleRequestDto.getReservationTableCount();
		this.reservationTime = reservationScheduleRequestDto.getReservationTime();
		this.reservationDate = reservationScheduleRequestDto.getReservationDate();
		this.currentCapacity = reservationScheduleRequestDto.getCurrentCapacity();
		this.tableTwo = reservationScheduleRequestDto.getTableTwo();
		this.tableFour = reservationScheduleRequestDto.getTableFour();
	}

	public void update(ReservationScheduleRequestDto requestDto) {
		this.storeId = requestDto.getStoreId();
		this.tableCount = requestDto.getTableCount();
		this.reservationTableCount = requestDto.getReservationTableCount();
		this.reservationTime = requestDto.getReservationTime();
		this.reservationDate = requestDto.getReservationDate();
		this.currentCapacity = requestDto.getCurrentCapacity();
		this.tableTwo = requestDto.getTableTwo();
		this.tableFour = requestDto.getTableFour();
	}
}
