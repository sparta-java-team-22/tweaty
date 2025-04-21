package com.tweaty.coupon.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweaty.coupon.domain.model.CouponIssue;
import com.tweaty.coupon.domain.repository.CouponIssueRepository;

public interface JpaCouponIssueRepository extends JpaRepository<CouponIssue, UUID>, CouponIssueRepository {
	boolean existsByCouponIdAndCustomerId(UUID couponId, UUID customerId);

	Optional<CouponIssue> findByCouponIdAndCustomerId(UUID couponId, UUID customerId);
}
