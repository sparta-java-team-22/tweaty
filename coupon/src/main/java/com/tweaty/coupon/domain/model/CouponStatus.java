package com.tweaty.coupon.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CouponStatus {
	UNUSED("미사용"),
	USED("사용");

	private final String name;
}
