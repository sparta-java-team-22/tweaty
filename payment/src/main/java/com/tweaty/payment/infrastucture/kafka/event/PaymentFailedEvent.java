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
public class PaymentFailedEvent {
	private UUID paymentId;
	private UUID userId;
	private UUID storeId;

	public static PaymentFailedEvent toDto(Payment payment) {
		return PaymentFailedEvent.builder()
			.paymentId(payment.getId())
			.userId(payment.getUserId())
			.storeId(payment.getStoreId())
			.build();
	}
}