package com.tweaty.payment.presentation.dto.request;

import java.util.UUID;

import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.domain.entity.RefundType;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class RefundRequestDto {

	private String reason;
	private UUID couponId;

}
