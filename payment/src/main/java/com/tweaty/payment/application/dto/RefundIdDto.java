package com.tweaty.payment.application.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefundIdDto {
	private UUID refundId;
}
