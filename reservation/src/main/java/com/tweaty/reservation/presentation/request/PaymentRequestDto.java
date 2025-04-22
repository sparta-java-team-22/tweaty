package com.tweaty.reservation.presentation.request;

import java.util.UUID;

import com.tweaty.reservation.domain.model.MethodType;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class PaymentRequestDto {

	private int originalAmount;
	private UUID couponId;
	private MethodType method;
}
