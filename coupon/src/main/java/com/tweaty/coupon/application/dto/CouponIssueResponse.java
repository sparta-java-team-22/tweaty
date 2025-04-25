package com.tweaty.coupon.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.coupon.domain.model.Coupon;
import com.tweaty.coupon.domain.model.CouponIssue;

public record CouponIssueResponse(
	UUID couponIssueId,
	LocalDateTime couponIssuedAt,
	LocalDateTime couponExpiredAt,
	UUID couponId,
	String couponName
) {
	public static CouponIssueResponse from(CouponIssue couponIssue, Coupon coupon) {
		return new CouponIssueResponse(
			couponIssue.getCouponIssueId(),
			couponIssue.getCouponIssueAt(),
			couponIssue.getCouponExpiryAt(),
			coupon.getCouponId(),
			coupon.getCouponName()
		);
	}
}
