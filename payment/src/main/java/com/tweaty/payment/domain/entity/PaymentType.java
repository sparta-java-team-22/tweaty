package com.tweaty.payment.domain.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentType {

	READY("결제 요청 준비"), SUCCESS("결제완료"), FAIL("결제실패"),CANCEL("결제취소"), REFUNDED("환불완료");

	private final String label;
}
