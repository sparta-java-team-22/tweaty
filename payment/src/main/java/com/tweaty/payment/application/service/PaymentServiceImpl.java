package com.tweaty.payment.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.domain.service.PaymentDomainService;
import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentDomainService paymentDomainService;

	@Override
	@Transactional
	public UUID createPayment(PaymentRequestDto req, UUID userId, UUID storeId) {

		// 1. 결제 객체 생성(결제요청 상태)
		Payment payment = Payment.toReadyEntity(req, storeId,userId);
		saveReadyPayment(payment);

		try {
			// 2. 쿠폰이 있는 경우에 할인 적용
			// TODO: coupon-service: 쿠폰아이디가 있으면 요청보내서 할인금액 받아오기
			if (req.getCouponId() != null) {
				int discountAmount = 1000;
				// TODO: 할인금액 계산하기 -> PaymentDomainService 에서 구현
				int finalAmount = paymentDomainService.calculateDiscount(req.getOriginalAmount(), discountAmount);
				payment.applyDiscount(discountAmount, finalAmount);
			}

			// 3. 결제 성공 처리 + 저장
			payment.successPayment();

		} catch (Exception e) {
			payment.failPayment();
			log.error("결제실패 : {}", e.getMessage());
		}

		paymentRepository.save(payment);
		return payment.getId();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveReadyPayment(Payment payment) {
		paymentRepository.save(payment);
	}

}
