package com.tweaty.coupon.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CouponIssueStatus {
	PENDING(false, "아직 쿠폰이 발급되지 않았습니다."),
	SUCCESS(true, "쿠폰이 성공적으로 발급되었습니다.");

	private final boolean isIssued;
	private final String couponStatusMessage;
}
