package com.tweaty.coupon.infrastructure.kafka.event;

import java.util.UUID;

public record TimeAttackCouponCreateEvent(
	UUID couponId,
	UUID userId
) {
}
