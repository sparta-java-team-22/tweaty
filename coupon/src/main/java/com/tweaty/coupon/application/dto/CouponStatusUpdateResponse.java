package com.tweaty.coupon.application.dto;

import com.tweaty.coupon.domain.model.CouponIssue;
import com.tweaty.coupon.domain.model.CouponStatus;

public record CouponStatusUpdateResponse(CouponStatus couponStatus) {
	public static CouponStatusUpdateResponse from(CouponIssue couponIssue) {
		return new CouponStatusUpdateResponse(couponIssue.getCouponStatus());
	}
}