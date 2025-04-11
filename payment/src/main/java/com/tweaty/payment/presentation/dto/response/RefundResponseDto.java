package com.tweaty.payment.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.payment.domain.entity.MethodType;
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.Refund;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RefundResponseDto {

	@NotEmpty
	private UUID id;
	private int amount;
	private String reason;
	private LocalDateTime createdAt;


	@Builder
	public static RefundResponseDto toDto(Refund refund) {
		return RefundResponseDto.builder()
			.id(refund.getId())
			.amount(refund.getAmount())
			.reason(refund.getReason())
			.createdAt(refund.getCreatedAt())
			.build();
	}
}
