package com.tweaty.payment.infrastucture.kafka.event;

import java.util.UUID;

import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreateEvent {
	private UUID userId;
	private UUID storeId;
	private UUID couponId;
	private int originalAmount;
	private String method;

	public static PaymentCreateEvent of(PaymentRequestDto req, UUID userId, UUID storeId) {
		return PaymentCreateEvent.builder()
			.userId(userId)
			.storeId(storeId)
			.couponId(req.getCouponId())
			.originalAmount(req.getOriginalAmount())
			.method(req.getMethod().name())
			.build();
	}
}