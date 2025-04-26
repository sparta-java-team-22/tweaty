package com.tweaty.coupon.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.coupon.domain.model.CouponIssue;

public record CouponIssueResponse(
	UUID couponIssueId,
	LocalDateTime couponIssuedAt,
	LocalDateTime couponExpiredAt
) {
	public static CouponIssueResponse from(CouponIssue couponIssue) {
		return new CouponIssueResponse(
			couponIssue.getCouponIssueId(),
			couponIssue.getCouponIssueAt(),
			couponIssue.getCouponExpiryAt()
		);
	}
}
