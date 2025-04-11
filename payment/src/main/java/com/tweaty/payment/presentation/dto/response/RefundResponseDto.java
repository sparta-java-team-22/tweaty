package com.tweaty.payment.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.payment.domain.entity.MethodType;

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
	@NotEmpty
	private UUID userId;
	private int amount;
	private String reason;
	private LocalDateTime createdAt;
}
