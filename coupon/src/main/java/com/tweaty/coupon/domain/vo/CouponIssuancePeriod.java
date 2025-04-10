package com.tweaty.coupon.domain.vo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class CouponIssuancePeriod {
	@Column(name = "coupon_issuance_start_at")
	private LocalDateTime startAt;

	@Column(name = "coupon_issance_end_at")
	private LocalDateTime endAt;

	public CouponIssuancePeriod(LocalDateTime startAt, LocalDateTime endAt) {
		if (startAt == null || endAt == null || startAt.isAfter(endAt)) {
			throw new IllegalArgumentException("시작일은 종료일보다 앞서야 합니다.");
		}
		this.startAt = startAt;
		this.endAt = endAt;
	}

	public static CouponIssuancePeriod of(LocalDateTime startAt, LocalDateTime endAt) {
		return new CouponIssuancePeriod(startAt, endAt);
	}
}
