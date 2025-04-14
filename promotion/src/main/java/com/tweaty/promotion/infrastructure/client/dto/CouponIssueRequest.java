package com.tweaty.promotion.infrastructure.client.dto;

import java.time.LocalDateTime;

import com.tweaty.promotion.domain.model.Promotion;

public record CouponIssueRequest(LocalDateTime couponExpiryAt) {
	public static CouponIssueRequest from(Promotion promotion) {
		return new CouponIssueRequest(promotion.getEventPeriod().getEndAt());
	}
}
