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

	@Column(name = "reservation_schedule_id", nullable = false)
	private UUID reservationScheduleId;

	@Column(name = "store_id", nullable = false)
	private UUID storeId;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "guest_count", nullable = false)
	private int guestCount;

	@Column(name = "status", nullable = false)
	private ReservationStatus status;

	public Reservation(ReservationRequestDto requestDto, UUID userId) {
		this.storeId = requestDto.getStoreId();
		this.reservationScheduleId = requestDto.getReservationScheduleId();
		this.userId = userId;
		this.guestCount = requestDto.getGuestCount();
		this.status = ReservationStatus.COMPLETED;
	}

	public void update(ReservationRequestDto requestDto) {
		this.guestCount = requestDto.getGuestCount();
	}
}
