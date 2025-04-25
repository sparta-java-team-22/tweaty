package com.tweaty.promotion.infrastructure.kafka.event;

import java.util.UUID;

public record TimeAttackCouponCreateEvent(
	UUID couponId,
	UUID userId
) {
	public static TimeAttackCouponCreateEvent from(
		UUID couponId,
		UUID userId
	) {
		return new TimeAttackCouponCreateEvent(couponId, userId);
	}
}
