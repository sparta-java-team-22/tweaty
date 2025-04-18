package com.tweaty.reservation.domain.model;

import java.util.UUID;

import com.tweaty.reservation.presentation.request.ReservationRequestDto;

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
public class Reservation extends BaseEntity {
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

	public Reservation(ReservationRequestDto reservationRequestDto) {
		this.storeId = reservationRequestDto.getStoreId();
		this.tableCount = reservationRequestDto.getTableCount();
		this.reservationTableCount = reservationRequestDto.getReservationTableCount();
		this.reservationTime = reservationRequestDto.getReservationTime();
		this.reservationDate = reservationRequestDto.getReservationDate();
		this.currentCapacity = reservationRequestDto.getCurrentCapacity();
		this.tableTwo = reservationRequestDto.getTableTwo();
		this.tableFour = reservationRequestDto.getTableFour();
	}

	public void update(ReservationRequestDto requestDto) {
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
