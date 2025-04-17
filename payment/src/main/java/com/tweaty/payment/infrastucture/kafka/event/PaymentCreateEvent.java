package com.tweaty.payment.infrastucture.kafka.event;

import java.util.UUID;

import com.tweaty.payment.domain.entity.Payment;
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
	private UUID paymentId;
	private UUID userId;
	private UUID reservationId;
	private UUID couponId;
	private int originalAmount;
	private String method;

	public static PaymentCreateEvent of(PaymentRequestDto req, UUID userId, UUID reservationId) {
		return PaymentCreateEvent.builder()
			.userId(userId)
			.reservationId(reservationId)
			.couponId(req.getCouponId())
			.originalAmount(req.getOriginalAmount())
			.method(req.getMethod().name())
			.build();
	}

	public static PaymentCreateEvent from(Payment payment) {
		return PaymentCreateEvent.builder()
			.paymentId(payment.getId())
			.userId(payment.getUserId())
			.reservationId(payment.getReservationId())
			.couponId(payment.getCouponId())
			.originalAmount(payment.getOriginalAmount())
			.method(payment.getMethod().name())
			.build();
	}
}