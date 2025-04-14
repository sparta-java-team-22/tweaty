package com.tweaty.payment.domain.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DiscountType {

	FIXED("정액"), RATE("정률");

	private final String label;

}
