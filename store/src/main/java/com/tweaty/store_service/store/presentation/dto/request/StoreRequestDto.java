package com.tweaty.store_service.store.presentation.dto.request;

import java.util.UUID;

import com.tweaty.store_service.store.domain.entity.Store;
import com.tweaty.store_service.store.domain.entity.StoreType;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class StoreRequestDto {

	@NotEmpty
	private String name;
	@NotEmpty
	private String address;
	@NotEmpty
	private String phoneNumber;
	private String description;
	@NotEmpty
	private String openTime;
	@NotEmpty
	private String closedTime;

	@NotEmpty
	private StoreType status;
	@NotEmpty
	private boolean isReservation;
	@NotEmpty
	private boolean isWaiting;

	private int reservationAmount;

	private String imgUrl;

	@Builder
	public Store toEntity(UUID userId) {
		return Store.builder()
			.userId(userId)
			.name(this.name)
			.address(this.address)
			.phoneNumber(this.phoneNumber)
			.description(this.description)
			.openTime(this.openTime)
			.closedTime(this.closedTime)
			.status(this.status)
			.isReservation(this.isReservation)
			.isWaiting(this.isWaiting)
			.reservationAmount(this.reservationAmount)
			.imgUrl(this.imgUrl)
			.build();
	}
}
