package com.tweaty.coupon.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweaty.coupon.domain.model.Coupon;
import com.tweaty.coupon.domain.repository.CouponRepository;

public interface JpaCouponRepository extends JpaRepository<Coupon, UUID>, CouponRepository {
}
