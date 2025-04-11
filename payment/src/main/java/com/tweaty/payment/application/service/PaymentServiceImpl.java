package com.tweaty.payment.application.service;

import java.awt.*;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;
import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.domain.repository.RefundRepository;
import com.tweaty.payment.domain.service.PaymentDomainService;
import com.tweaty.payment.global.exception.CustomException;
import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;
import com.tweaty.payment.presentation.dto.request.RefundRequestDto;
import com.tweaty.payment.presentation.dto.response.PaymentResponseDto;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentDomainService paymentDomainService;

	private final RefundRepository refundRepository;

	@Override
	@Transactional
	public UUID createPayment(PaymentRequestDto req, UUID userId, UUID storeId) {

		// 1. 결제 객체 생성(결제요청 상태)
		Payment payment = Payment.toReadyEntity(req, storeId, userId);
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

			// 3. 결제 성공 처리
			payment.successPayment();
			paymentRepository.save(payment);

		} catch (Exception e) {
			log.error("결제실패 : {}", e.getMessage());
			payment.failPayment();
			paymentRepository.save(payment);
			throw new CustomException(ErrorCode.PAYMENT_FAIL, HttpStatus.BAD_REQUEST);
		}
		return payment.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PaymentResponseDto> getPayment(UUID userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		return paymentRepository.findByUserIdAndIsDeletedFalseAndStatus(userId, PaymentType.SUCCESS, pageable)
			.map(PaymentResponseDto::toDto);
	}

	@Override
	@Transactional
	public UUID createRefund(RefundRequestDto req, UUID userId, UUID paymentId) {
		// 1. 환불 객체 생성(환불요청 상태)
		Payment payment = findPayment(paymentId);
		Refund refund = Refund.toReadyEntity(req, userId, payment);
		saveReadyRefund(refund);

		try {
			// TODO: 2. coupon-service: 쿠폰아이디가 있으면 요청보내서 사용가능하게 만들기
			if (req.getCouponId() != null) {

			}
			// 3. 환불 성공 처리
			refund.successRefund();
			payment.successRefund();
			refundRepository.save(refund);
		} catch (Exception e) {
			log.error("환불 실패 : {}", e.getMessage());
			refund.failRefund();
			refundRepository.save(refund);
			throw new CustomException(ErrorCode.REFUND_FAIL, HttpStatus.BAD_REQUEST);
		}
		return refund.getId();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveReadyPayment(Payment payment) {
		paymentRepository.save(payment);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveReadyRefund(Refund refund) {
		refundRepository.save(refund);
	}

	private Payment findPayment(UUID paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (payment.getStatus() == PaymentType.REFUNDED) {
			throw new CustomException(ErrorCode.REFUND_ALREADY_USED, HttpStatus.BAD_REQUEST);
		}

		return payment;
	}

}
