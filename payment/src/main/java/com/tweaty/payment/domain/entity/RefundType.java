package com.tweaty.payment.domain.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RefundType {

	READY("환불요청"), SUCCESS("성공"), FAIL("실패");

	private final String label;

}
