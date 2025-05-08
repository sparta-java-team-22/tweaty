package com.tweaty.payment.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
	private final RedissonClient redissonClient;


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

	@Transactional(readOnly = true)
	public Payment findPaymentWithLock(UUID paymentId) {
		Payment payment = paymentRepository.findByIdForUpdate(paymentId)
			.orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (payment.getStatus() == PaymentType.REFUNDED) {
			throw new CustomException(ErrorCode.REFUND_ALREADY_USED, HttpStatus.BAD_REQUEST);
		}

		return payment;
	}


	@Transactional(readOnly = true)
	public Payment findPaymentNature(UUID paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (payment.getStatus() == PaymentType.REFUNDED) {
			throw new CustomException(ErrorCode.REFUND_ALREADY_USED, HttpStatus.BAD_REQUEST);
		}

		return payment;
	}


	// 비관적 락 테스트
	@Transactional
	public void testWithPessimisticLock(UUID paymentId) {
		long start = System.currentTimeMillis();
		Payment payment = paymentRepository.findByIdForUpdate(paymentId)
			.orElseThrow();

		simulateWork("비관적락", paymentId);

		if (!payment.isCompleted()) {
			payment.successPayment();
		}
		long end = System.currentTimeMillis();
		System.out.println("🔒 [비관적락] 처리시간 = " + (end - start) + "ms");
	}

	// 분산 락 테스트
	public void testWithRedissonLock(UUID paymentId) {
		String lockKey = "payment-lock:" + paymentId;
		RLock lock = redissonClient.getLock(lockKey);

		long start = System.currentTimeMillis();
		boolean isLocked = false;
		try {
			isLocked = lock.tryLock(2, 5, TimeUnit.SECONDS);
			if (!isLocked) {
				System.out.println("❌ [Redisson] 락 획득 실패");
				return;
			}

			Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow();

			simulateWork("Redisson", paymentId);

			if (!payment.isCompleted()) {
				payment.successPayment();
				paymentRepository.save(payment);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (isLocked && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("🔓 [Redisson] 처리시간 = " + (end - start) + "ms");
	}

	private void simulateWork(String type, UUID paymentId) {
		System.out.println("▶ [" + type + "] 처리 시작: " + paymentId);
		try {
			Thread.sleep(200); // 로직 지연 시뮬레이션
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}

