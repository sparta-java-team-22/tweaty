package com.tweaty.payment.presentation.dto.request;


import java.util.UUID;

import com.tweaty.payment.domain.entity.MethodType;
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class PaymentRequestDto {

	@NotEmpty
	private int originalAmount;
	private UUID couponId;
	@NotEmpty
	private MethodType method;




	@Builder
	public Payment toEntity(UUID storeId,int discountAmount, int finalAmount) {
		return Payment.builder()
			.storeId(storeId)
			.couponId(this.couponId)
			.originalAmount(this.originalAmount)
			.discountAmount(discountAmount)
			.finalAmount(finalAmount)
			.status(PaymentType.READY)
			.method(this.method)
			.build();
	}


}
