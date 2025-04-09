package com.tweaty.coupon.domain.repository;

import com.tweaty.coupon.domain.model.Coupon;

public interface CouponRepository {
	Coupon save(Coupon coupon);
}
