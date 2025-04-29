package com.tweaty.reservation.presentation.request;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class RefundRequestDto {

	private String reason;
	private UUID couponId;

}
