package com.tweaty.coupon.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.coupon.domain.model.CouponIssue;

public record CouponIssueStatusResponse(
	UUID couponId,
	UUID customerId,
	LocalDateTime issuedAt,
	boolean isIssued,
	CouponIssueStatus couponIssueStatus,
	String couponStatusMessage
) {
	public static CouponIssueStatusResponse from(
		CouponIssue couponIssue,
		CouponIssueStatus couponIssueStatus
	) {
		return new CouponIssueStatusResponse(
			couponIssue.getCouponId(),
			couponIssue.getCustomerId(),
			couponIssue.getCouponIssueAt(),
			couponIssueStatus.isIssued(),
			couponIssueStatus,
			couponIssueStatus.getCouponStatusMessage()
		);
	}

	public static CouponIssueStatusResponse notIssued(UUID couponId, UUID customerId) {
		return new CouponIssueStatusResponse(
			couponId,
			customerId,
			null,
			Boolean.FALSE,
			CouponIssueStatus.PENDING,
			CouponIssueStatus.PENDING.getCouponStatusMessage()
		);
	}
}
