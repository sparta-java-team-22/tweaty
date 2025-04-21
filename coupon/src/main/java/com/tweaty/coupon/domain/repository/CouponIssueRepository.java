package com.tweaty.coupon.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.tweaty.coupon.domain.model.CouponIssue;

public interface CouponIssueRepository {
	boolean existsByCouponIdAndCustomerId(UUID couponId, UUID customerId);

	CouponIssue save(CouponIssue couponIssue);

	Optional<CouponIssue> findByCouponIdAndCustomerId(UUID couponId, UUID customerId);
}
