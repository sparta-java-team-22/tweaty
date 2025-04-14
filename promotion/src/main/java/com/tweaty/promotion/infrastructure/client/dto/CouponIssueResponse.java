package com.tweaty.promotion.infrastructure.client.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CouponIssueResponse(
	UUID couponIssueId,
	LocalDateTime couponIssuedAt,
	LocalDateTime couponExpiredAt
) {
}
