package com.tweaty.store_service.store.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
@Builder
@Table(name = "p_store")
public class Store {
	@Id
	@UuidGenerator
	@Column(name = "store_id")
	private UUID id;
	@Column(nullable = false)
	private Long userId;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String address;
	private String	phoneNumber;
	private String description;
	@Column(nullable = false)
	private String	openTime;
	@Column(nullable = false)
	private String closedTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StoreType status;
	@Column(nullable = false)
	private boolean isReservation;
	@Column(nullable = false)
	private boolean	isWaiting;
	@Column(nullable = false)
	@ColumnDefault("0")
	private int reservationAmount;

	private String	imgUrl;



}
