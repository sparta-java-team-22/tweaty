package com.tweaty.payment.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;
import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.domain.entity.RefundType;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.domain.repository.RefundRepository;
import com.tweaty.payment.domain.service.PaymentDomainService;
import com.tweaty.payment.global.exception.CustomException;
import com.tweaty.payment.infrastucture.client.CouponClient;

import com.tweaty.payment.presentation.dto.request.RefundRequestDto;
import com.tweaty.payment.presentation.dto.response.CouponReadResponse;
import com.tweaty.payment.presentation.dto.response.PaymentResponseDto;
import com.tweaty.payment.presentation.dto.response.RefundResponseDto;

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

	private final CouponClient couponClient;

	@Override
	@Transactional
	public UUID createPayment(Payment payment) {

		try {
			// 쿠폰이 있는 경우에 할인 적용
			if (payment.getCouponId() != null) {
				CouponReadResponse coupon = couponClient.getCoupon(payment.getCouponId());
				int finalAmount = paymentDomainService.calculateDiscount(payment.getOriginalAmount(),
					coupon.discountAmount(),
					coupon.discountType());
				payment.applyDiscount(coupon.discountAmount(), finalAmount);
			}


			// 결제 성공 처리
			payment.successPayment();
			paymentRepository.save(payment);

		} catch (Exception e) {
			log.error("결제 실패 : {}", e.getMessage());
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

		return paymentRepository.findByUserIdAndStatus(userId, PaymentType.SUCCESS, pageable)
			.map(PaymentResponseDto::toDto);
	}

	@Override
	@Transactional
	public UUID createRefund(RefundRequestDto req, UUID userId, UUID paymentId) {
		// 1. 환불 객체 생성(환불요청 상태)
		Payment payment = paymentDomainService.findPayment(paymentId);
		Refund refund = Refund.toReadyEntity(req, userId, payment);
		paymentDomainService.saveReadyRefund(refund);

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

	@Override
	@Transactional(readOnly = true)
	public Page<RefundResponseDto> getRefund(UUID userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		return refundRepository.findByUserIdAndStatus(userId, RefundType.SUCCESS, pageable)
			.map(RefundResponseDto::toDto);
	}

}
