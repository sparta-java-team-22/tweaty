package com.tweaty.reservation.domain.model;

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
	private String id;

	@Column(name = "store_id", nullable = false)
	private String storeId;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "guest_count", nullable = false)
	private int guestCount;

	@Column(name = "status", nullable = false)
	private ReservationStatus status;

}
