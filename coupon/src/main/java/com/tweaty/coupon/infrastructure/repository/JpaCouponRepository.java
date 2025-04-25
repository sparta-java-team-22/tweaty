package com.tweaty.coupon.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.tweaty.coupon.domain.model.Coupon;
import com.tweaty.coupon.domain.repository.CouponRepository;

import jakarta.persistence.LockModeType;

public interface JpaCouponRepository extends JpaRepository<Coupon, UUID>, CouponRepository {
	Optional<Coupon> findByCouponId(UUID couponId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Coupon c WHERE c.couponId = :couponId")
	Coupon findByIdWithPessimisticLock(UUID couponId);
}
