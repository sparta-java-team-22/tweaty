package com.tweaty.coupon.application.dto;

import java.util.UUID;

import com.tweaty.coupon.domain.model.Coupon;

public record CouponCreateResponse(UUID couponId) {
	public static CouponCreateResponse from(Coupon coupon) {
		return new CouponCreateResponse(coupon.getCouponId());
	}
}
