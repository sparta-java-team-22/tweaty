package com.tweaty.promotion.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.promotion.infrastructure.client.dto.CouponIssueResponse;

public record PromotionTimeAttackCouponResponse(
	UUID couponIssueId,
	LocalDateTime couponIssuedAt,
	LocalDateTime couponExpiredAt,
	Boolean isCouponIssued
) {
	public static PromotionTimeAttackCouponResponse from(CouponIssueResponse couponIssueResponse) {
		return new PromotionTimeAttackCouponResponse(
			couponIssueResponse.couponIssueId(),
			couponIssueResponse.couponIssuedAt(),
			couponIssueResponse.couponExpiredAt(),
			true
		);
	}
}
