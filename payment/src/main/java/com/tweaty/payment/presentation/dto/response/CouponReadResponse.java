package com.tweaty.payment.presentation.dto.response;

import java.time.LocalDateTime;

import com.tweaty.payment.domain.entity.DiscountType;

import lombok.AllArgsConstructor;
import lombok.Getter;


public record CouponReadResponse(
	String couponName,
	DiscountType discountType,
	Integer discountAmount,
	Integer couponMaxIssuance,
	LocalDateTime couponIssuanceStartAt,
	LocalDateTime couponIssuanceEndAt,
	Boolean isFirstCome
) {

}