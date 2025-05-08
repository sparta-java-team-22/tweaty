package com.tweaty.coupon.domain.repository;

import java.util.UUID;

import com.tweaty.coupon.domain.model.Coupon;

public interface CouponCacheRepository {
	Long tryIssueCoupon(UUID couponId, UUID customerId, Long issuedKeyTtlSeconds);

	void cacheCoupon(Coupon coupon);
}
