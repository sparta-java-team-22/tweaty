package com.tweaty.payment.infrastucture.kafka.event;

import java.util.UUID;

import com.tweaty.payment.domain.entity.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSuccessEvent {
	private UUID paymentId;
	private UUID userId;
	private UUID storeId;
	private int finalAmount;
	private String method;



	public static PaymentSuccessEvent toDto(Payment payment) {
		return PaymentSuccessEvent.builder()
			.paymentId(payment.getId())
			.userId(payment.getUserId())
			.storeId(payment.getStoreId())
			.finalAmount(payment.getFinalAmount())
			.method(payment.getMethod().name())
			.build();
	}
}