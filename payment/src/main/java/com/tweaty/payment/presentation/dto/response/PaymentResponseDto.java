package com.tweaty.payment.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.payment.domain.entity.MethodType;
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PaymentResponseDto {
	@NotEmpty
	private UUID id;
	@NotEmpty
	private UUID reservationId;
	private UUID couponId;

	@NotEmpty
	private int finalAmount;
	@NotEmpty
	private MethodType method;

	private LocalDateTime createdAt;

	@Builder
	public static PaymentResponseDto toDto(Payment payment) {
		return PaymentResponseDto.builder()
			.id(payment.getId())
			.reservationId(payment.getReservationId())
			.couponId(payment.getCouponId())
			.finalAmount(payment.getFinalAmount())
			.method(payment.getMethod())
			.createdAt(payment.getCreatedAt())
			.build();
	}

}
