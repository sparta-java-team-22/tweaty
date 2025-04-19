package com.tweaty.payment.infrastucture.kafka.event;

import java.util.UUID;

import com.tweaty.payment.domain.entity.Refund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundSuccessEvent {
	private UUID refundId;
	private UUID userId;
	private UUID paymentId;
	private int refundAmount;

	public static RefundSuccessEvent toDto(Refund refund) {
		return RefundSuccessEvent.builder()
			.refundId(refund.getId())
			.userId(refund.getUserId())
			.paymentId(refund.getPayment().getId())
			.refundAmount(refund.getAmount())
			.build();
	}
}
