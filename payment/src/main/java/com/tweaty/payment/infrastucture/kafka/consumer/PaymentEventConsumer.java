package com.tweaty.payment.infrastucture.kafka.consumer;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.payment.domain.entity.DiscountType;
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.domain.service.PaymentDomainService;
import com.tweaty.payment.global.exception.CustomException;
import com.tweaty.payment.infrastucture.client.CouponClient;
import com.tweaty.payment.infrastucture.kafka.event.PaymentCreateEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentFailedEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentSuccessEvent;
import com.tweaty.payment.infrastucture.kafka.producer.KafkaPaymentProducer;
import com.tweaty.payment.presentation.dto.response.CouponReadResponse;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

	private final PaymentRepository paymentRepository;
	private final PaymentDomainService paymentDomainService;
	private final KafkaPaymentProducer kafkaPaymentProducer;
	private final CouponClient couponClient;
	private final RedissonClient redissonClient;

	@KafkaListener(topics = "payment-success", groupId = "payment-consumer")
	public void consumePaymentSuccess(PaymentSuccessEvent event) {
		log.info("결제 성공 이벤트 수신: {}", event);

		// 여기서 알림 전송, 포인트 적립 등 처리
	}

	@KafkaListener(topics = "payment-failed", groupId = "alert-service")
	public void consumePaymentFailed(PaymentFailedEvent event) {
		log.warn("결제 실패 이벤트 수신: {}", event);
		// 알림 전송, 로그 저장, 슬랙 메시지 발송 등 처리 가능
	}

	@KafkaListener(topics = "payment-create-test", groupId = "payment-service")
	@Transactional
	public void handleCreatePaymentNature(PaymentCreateEvent event) {
		log.info("결제 생성 이벤트 수신: {}", event);

		Payment payment = paymentDomainService.findPaymentNature(event.getPaymentId());

		if (payment.isCompleted()) {
			log.info("이미 처리된 결제입니다. paymentId: {}", payment.getId());
			return;
		}
		try {
			if (event.getCouponId() != null) {
				CouponReadResponse coupon = couponClient.getCoupon(event.getCouponId());
				int finalAmount = paymentDomainService.calculateDiscount(
					event.getOriginalAmount(), coupon.discountAmount(), coupon.discountType()
				);
				payment.applyDiscount(coupon.discountAmount(), finalAmount);
			}
			if (payment.isCompleted()) {
				log.warn("결제 처리 도중 상태 변경됨. 중복 처리 방지. paymentId: {}", payment.getId());
				return;
			}

			payment.successPayment();
			paymentRepository.save(payment);
			log.info(" [Kafka 처리 완료] Payment 저장");

			kafkaPaymentProducer.sendSuccessEvent(PaymentSuccessEvent.toDto(payment));

		} catch (Exception e) {
			log.error(" 결제 실패: {}", e.getMessage());
			payment.failPayment();
			paymentRepository.save(payment);

			kafkaPaymentProducer.sendFailedEvent(PaymentFailedEvent.toDto(payment));
		}
	}

	@KafkaListener(topics = "payment-create", groupId = "payment-service")
	@Transactional
	public void handleCreatePayment(PaymentCreateEvent event) {
		log.info("결제 생성 이벤트 수신: {}", event);

		Payment payment = paymentDomainService.findPaymentWithLock(event.getPaymentId());

		if (payment.isCompleted()) {
			log.info("이미 처리된 결제입니다. paymentId: {}", payment.getId());
			return;
		}

		try {
			if (event.getCouponId() != null) {
				CouponReadResponse coupon = couponClient.getCoupon(event.getCouponId());
				int finalAmount = paymentDomainService.calculateDiscount(
					event.getOriginalAmount(), coupon.discountAmount(), coupon.discountType()
				);
				payment.applyDiscount(coupon.discountAmount(), finalAmount);
			}
			if (payment.isCompleted()) {
				log.warn("결제 처리 도중 상태 변경됨. 중복 처리 방지. paymentId: {}", payment.getId());
				return;
			}

			payment.successPayment();
			paymentRepository.save(payment);
			log.info(" [Kafka 처리 완료] Payment 저장");

			kafkaPaymentProducer.sendSuccessEvent(PaymentSuccessEvent.toDto(payment));

		} catch (Exception e) {
			log.error(" 결제 실패: {}", e.getMessage());
			payment.failPayment();
			paymentRepository.save(payment);

			kafkaPaymentProducer.sendFailedEvent(PaymentFailedEvent.toDto(payment));
		}
	}

	// @KafkaListener(topics = "payment-create-redisson", groupId = "payment-service")
	// @Transactional
	// public void handleCreatePaymentByRedisson(PaymentCreateEvent event) {
	//
	// 	log.info("결제 생성 이벤트 수신: {}", event);
	//
	// 	String lockKey = "payment-lock:" + event.getUserId() + ":" + event.getReservationId();
	// 	RLock lock = redissonClient.getLock(lockKey);
	// 	boolean isLocked = false;
	//
	// 	// 1. 락 밖에서 쿠폰 검증 및 할인 계산
	// 	int discountAmount = 0;
	// 	int finalAmount = event.getOriginalAmount();
	//
	// 	try {
	// 		if (event.getCouponId() != null) {
	// 			CouponReadResponse coupon = couponClient.getCoupon(event.getCouponId());
	// 			discountAmount = coupon.discountAmount();
	// 			finalAmount = paymentDomainService.calculateDiscount(
	// 				event.getOriginalAmount(), coupon.discountAmount(), coupon.discountType()
	// 			);
	// 		}
	// 	} catch (Exception e) {
	// 		log.error("쿠폰 검증 실패: {}", e.getMessage());
	// 		// 쿠폰 실패 시 결제 중단 or 무할인으로 진행 여부 결정
	// 		return;
	// 	}
	//
	// 	// 2. 락 획득 시도 + 시간 측정
	// 	long start = System.currentTimeMillis();
	// 	try {
	// 		isLocked = lock.tryLock(2, 5, TimeUnit.SECONDS);
	// 	} catch (InterruptedException e) {
	// 		log.error("락 획득 중 인터럽트 발생", e);
	// 		Thread.currentThread().interrupt();
	// 		return;
	// 	}
	// 	long end = System.currentTimeMillis();
	// 	log.info("락 획득: {}, 대기 시간: {}ms", isLocked, (end - start));
	//
	// 	if (!isLocked) {
	// 		log.warn("락 획득 실패: 처리 중단. paymentId={}", event.getPaymentId());
	// 		return;
	// 	}
	//
	// 	try {
	// 		// 3. 락 안에서 최소한의 작업: payment 상태 확인 + 처리
	// 		Payment payment = paymentDomainService.findPayment(event.getPaymentId());
	//
	// 		if (payment.isCompleted()) {
	// 			log.info("이미 완료된 결제입니다. paymentId: {}", payment.getId());
	// 			return;
	// 		}
	//
	// 		payment.applyDiscount(discountAmount, finalAmount);
	// 		payment.successPayment();
	// 		paymentRepository.save(payment);
	// 		log.info("[Kafka 처리 완료] Payment 저장");
	//
	// 		kafkaPaymentProducer.sendSuccessEvent(PaymentSuccessEvent.toDto(payment));
	//
	// 	} catch (Exception e) {
	// 		log.error("결제 처리 중 예외 발생: {}", e.getMessage());
	//
	// 		try {
	// 			Payment payment = paymentDomainService.findPayment(event.getPaymentId());
	// 			if (!payment.isCompleted()) {
	// 				payment.failPayment();
	// 				paymentRepository.save(payment);
	// 				kafkaPaymentProducer.sendFailedEvent(PaymentFailedEvent.toDto(payment));
	// 			}
	// 		} catch (Exception ex) {
	// 			log.error("결제 실패 상태 저장 중 오류: {}", ex.getMessage());
	// 		}
	// 	} finally {
	// 		if (lock.isHeldByCurrentThread()) {
	// 			lock.unlock();
	// 		}
	// 	}
	// }

	@KafkaListener(topics = "payment-create-redisson", groupId = "payment-service")
	@Transactional
	public void handleCreatePaymentByRedisson(PaymentCreateEvent event) {

		log.debug("결제 생성 이벤트 수신: {}", event);

		String lockKey = "payment-lock:" + event.getUserId() + ":" + event.getReservationId();
		RLock lock = redissonClient.getLock(lockKey);
		boolean isLocked = false;
		try {
			// 2초 동안 락 획득 시도, 5초 동안 점유
			isLocked = lock.tryLock(2, 5, TimeUnit.SECONDS);

			if (!isLocked) {
				log.warn("락 획득 실패: 결제 처리 중단. paymentId={}", event.getPaymentId());
				return; // 락 못잡으면 처리하지 않음
			}

			Payment payment = paymentDomainService.findPayment(event.getPaymentId());

			if (payment.isCompleted()) {
				log.debug("이미 완료된 결제입니다. paymentId: {}", payment.getId());
				return;
			}

			try {
				if (event.getCouponId() != null) {
					CouponReadResponse coupon = couponClient.getCoupon(event.getCouponId());
					int finalAmount = paymentDomainService.calculateDiscount(
						event.getOriginalAmount(), coupon.discountAmount(), coupon.discountType()
					);
					payment.applyDiscount(coupon.discountAmount(), finalAmount);
				}

				payment.successPayment();
				paymentRepository.save(payment);
				log.debug("[Kafka 처리 완료] Payment 저장");

				kafkaPaymentProducer.sendSuccessEvent(PaymentSuccessEvent.toDto(payment));

			} catch (Exception e) {
				log.error("결제 실패: {}", e.getMessage());

				if (!payment.isCompleted()) {
					payment.failPayment();
					paymentRepository.save(payment);

					kafkaPaymentProducer.sendFailedEvent(PaymentFailedEvent.toDto(payment));
				}
			}

		} catch (InterruptedException e) {
			log.error("락 처리 중 인터럽트 발생", e);
			Thread.currentThread().interrupt();
		}
	}

}
