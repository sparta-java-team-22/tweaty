package com.tweaty.store_service.store.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import com.tweaty.store_service.store.presentation.dto.request.StoreRequestDto;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_store")
public class Store extends BaseEntity {
	@Id
	@UuidGenerator
	@Column(name = "store_id")
	private UUID id;
	@Column(nullable = false)
	private UUID userId;
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


	public void update(StoreRequestDto req) {
		this.name = req.getName();
		this.address = req.getAddress();
		this.phoneNumber = req.getPhoneNumber();
		this.description = req.getDescription();
		this.openTime = req.getOpenTime();
		this.closedTime = req.getClosedTime();
		this.status = req.getStatus();
		this.isReservation = req.isReservation();
		this.isWaiting = req.isWaiting();
		this.reservationAmount = req.getReservationAmount();
		this.imgUrl = req.getImgUrl();
	}

}
