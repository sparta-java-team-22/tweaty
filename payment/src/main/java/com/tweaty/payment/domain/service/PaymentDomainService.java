package com.tweaty.payment.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.payment.domain.entity.DiscountType;
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;
import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.domain.entity.RefundType;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.domain.repository.RefundRepository;
import com.tweaty.payment.global.exception.CustomException;
import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
		try {
			paymentRepository.save(payment);
		} catch (Exception e) {
			log.error("결제 에러발생", e);
			throw new CustomException(ErrorCode.PAYMENT_DUPLICATE, HttpStatus.CONFLICT);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveReadyRefund(Refund refund) {
		refundRepository.save(refund);
	}

	public Payment findPayment(UUID paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (payment.getStatus() == PaymentType.REFUNDED) {
			throw new CustomException(ErrorCode.REFUND_ALREADY_USED, HttpStatus.BAD_REQUEST);
		}

		return payment;
	}

	public Refund findRefund(UUID refundId, UUID userId) {
		Refund refund = refundRepository.findById(refundId)
			.orElseThrow(() -> new CustomException(ErrorCode.REFUND_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (!userId.equals(refund.getUserId())) {
			throw new CustomException(ErrorCode.USER_FORBIDDEN, HttpStatus.FORBIDDEN);
		}

		if (refund.getStatus() != RefundType.READY) {
			throw new CustomException(ErrorCode.REFUND_ALREADY_USED, HttpStatus.BAD_REQUEST);
		}

		return refund;
	}

	public Payment toReadyPayment(PaymentRequestDto req, UUID userId, UUID reservationId) {

		if (paymentRepository.existsByUserIdAndReservationId(userId, reservationId)) {
			throw new CustomException(ErrorCode.PAYMENT_DUPLICATE, HttpStatus.CONFLICT);
		}
		Payment payment = Payment.toReadyEntity(req, reservationId, userId);
		saveReadyPayment(payment);

		return payment;
	}

	public Boolean existsPayment( UUID userId, UUID reservationId) {

		return paymentRepository.existsByUserIdAndReservationId(userId, reservationId);
	}
}

