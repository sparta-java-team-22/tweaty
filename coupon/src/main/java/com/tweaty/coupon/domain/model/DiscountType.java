package com.tweaty.coupon.domain.model;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DiscountType {
	FIXED("정액"),
	RATE("정률"),
	NONE("할인 정책을 찾을 수 없음");

	private final String name;

	public static DiscountType from(String type) {
		return Arrays.stream(values())
			.filter(v -> v.name.equalsIgnoreCase(type))
			.findFirst()
			.orElse(NONE);
	}
}
