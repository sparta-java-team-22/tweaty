package com.tweaty.store_service.store.presentation.dto.response;

import java.util.UUID;

import com.tweaty.store_service.store.domain.entity.Store;
import com.tweaty.store_service.store.domain.entity.StoreType;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StoreResponseDto {

	@NotEmpty
	private UUID id;
	@NotEmpty
	private UUID userId;
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

	public static StoreResponseDto toDto(Store store) {
		return StoreResponseDto.builder()
			.id(store.getId())
			.userId(store.getUserId())
			.name(store.getName())
			.address(store.getAddress())
			.phoneNumber(store.getPhoneNumber())
			.description(store.getDescription())
			.openTime(store.getOpenTime())
			.closedTime(store.getClosedTime())
			.status(store.getStatus())
			.isReservation(store.isReservation())
			.isWaiting(store.isWaiting())
			.reservationAmount(store.getReservationAmount())
			.imgUrl(store.getImgUrl())
			.build();
	}

}


