package com.tweaty.coupon.application.dto;

import java.time.LocalDateTime;

import com.tweaty.coupon.domain.model.Coupon;
import com.tweaty.coupon.domain.model.DiscountType;

public record CouponReadResponse(
	String couponName,
	DiscountType discountType,
	Integer discountAmount,
	Integer couponMaxIssuance,
	LocalDateTime couponIssuanceStartAt,
	LocalDateTime couponIssuanceEndAt,
	Boolean isFirstCome
) {
	public static CouponReadResponse from(Coupon coupon) {
		return new CouponReadResponse(
			coupon.getCouponName(),
			coupon.getDiscountPolicy().getType(),
			coupon.getDiscountPolicy().getAmount(),
			coupon.getCouponMaxIssuance().getValue(),
			coupon.getCouponIssuancePeriod().getStartAt(),
			coupon.getCouponIssuancePeriod().getEndAt(),
			coupon.getIsFirstCome()
		);
	}
}
