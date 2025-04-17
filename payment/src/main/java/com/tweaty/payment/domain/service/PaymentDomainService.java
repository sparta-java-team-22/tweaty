package com.tweaty.payment.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.payment.domain.entity.DiscountType;
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.domain.repository.RefundRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentDomainService {

	private final PaymentRepository paymentRepository;
	private final RefundRepository refundRepository;

	public int calculateDiscount(int originalAmount, int discountAmount, DiscountType discountType) {
		// TODO: 할인로직 짜기
		int finalAmount = originalAmount;
		if (discountType == DiscountType.FIXED) {
			finalAmount -= discountAmount;
		} else if (discountType == DiscountType.RATE) {
			finalAmount -= (originalAmount * discountAmount / 100);
		} else if (discountType == DiscountType.NONE) {
		} else {
			throw new IllegalArgumentException("잘못된 할인 타입입니다.");
		}

		return Math.max(finalAmount, 0);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveReadyPayment(Payment payment) {
		paymentRepository.save(payment);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveReadyRefund(Refund refund) {
		refundRepository.save(refund);
	}
}
