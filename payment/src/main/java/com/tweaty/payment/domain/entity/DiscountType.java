package com.tweaty.payment.domain.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DiscountType {

	FIXED("정액"), RATE("정률"), NONE("할인 정책을 찾을 수 없음");

	private final String label;

}
