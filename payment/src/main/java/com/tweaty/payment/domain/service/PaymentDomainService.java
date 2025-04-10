package com.tweaty.payment.domain.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentDomainService {



	public int calculateDiscount(int originalAmount,int discountAmount) {
		// TODO: 할인로직 짜기


		return originalAmount-discountAmount;

	}
}
