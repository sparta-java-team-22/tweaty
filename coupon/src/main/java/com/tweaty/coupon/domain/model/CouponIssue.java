package com.tweaty.coupon.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_coupon_issue")
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
public class CouponIssue extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID couponIssueId;

	@Column(name = "coupon_id")
	private UUID couponId;

	@Column(name = "customer_id")
	private UUID customerId;

	@Column(name = "coupon_issue_at")
	private LocalDateTime couponIssueAt;

	@Column(name = "coupon_expiry_at")
	private LocalDateTime couponExpiryAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "coupon_status")
	private CouponStatus couponStatus;

	public static CouponIssue create(
		Coupon coupon,
		UUID customerId,
		LocalDateTime now
	) {
		return CouponIssue.builder()
			.couponId(coupon.getCouponId())
			.customerId(customerId)
			.couponIssueAt(LocalDateTime.now())
			.couponExpiryAt(now)
			.couponStatus(CouponStatus.UNUSED)
			.build();
	}

	public void updateUsedCouponStatus() {
		couponStatus = CouponStatus.USED;
	}

	public void updateUnusedCouponStatus() {
		couponStatus = CouponStatus.UNUSED;
	}
}
