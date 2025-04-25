package com.tweaty.promotion.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.promotion.infrastructure.client.dto.CouponIssueResponse;

public record PromotionTimeAttackCouponSyncResponse(
	UUID couponIssueId,
	LocalDateTime couponIssuedAt,
	LocalDateTime couponExpiredAt,
	Boolean isCouponIssued
) {
	public static PromotionTimeAttackCouponSyncResponse from(CouponIssueResponse couponIssueResponse) {
		return new PromotionTimeAttackCouponSyncResponse(
			couponIssueResponse.couponIssueId(),
			couponIssueResponse.couponIssuedAt(),
			couponIssueResponse.couponExpiredAt(),
			true
		);
	}
}
