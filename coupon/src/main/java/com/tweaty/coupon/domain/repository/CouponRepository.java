package com.tweaty.coupon.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.tweaty.coupon.domain.model.Coupon;

public interface CouponRepository {
	Coupon save(Coupon coupon);

	Optional<Coupon> findByCouponId(UUID couponId);

	Coupon findByIdWithPessimisticLock(UUID couponId);
}
