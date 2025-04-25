package com.tweaty.coupon.application;

import java.util.UUID;

import com.tweaty.coupon.application.dto.CouponIssueResponse;
import com.tweaty.coupon.application.dto.CouponIssueStatusResponse;

public interface CouponIssueService {
	CouponIssueResponse issueCouponV1WithLock(UUID couponId, UUID customerId);

	CouponIssueResponse issueCouponV2WithRedis(UUID couponId, UUID customerId);

	void updateCouponStock(UUID couponId);

	CouponIssueStatusResponse getCouponIssueStatus(UUID couponId, UUID customerId);
}
