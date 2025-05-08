package com.tweaty.promotion.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PromotionStatus {
	PENDING(true, "선착순 쿠폰이 발급 중입니다. 잠시만 기다려주세요."),
	FAILED(false, "선착순 쿠폰 발급이 실패했습니다.");

	private final Boolean isCouponIssued;
	private final String message;
}
