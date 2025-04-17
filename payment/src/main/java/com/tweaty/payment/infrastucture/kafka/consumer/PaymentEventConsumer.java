package com.tweaty.payment.infrastucture.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.payment.domain.entity.DiscountType;
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.domain.service.PaymentDomainService;
import com.tweaty.payment.infrastucture.kafka.event.PaymentCreateEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentFailedEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentSuccessEvent;
import com.tweaty.payment.infrastucture.kafka.producer.KafkaPaymentProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

	private final PaymentRepository paymentRepository;
	private final PaymentDomainService paymentDomainService;
	private final KafkaPaymentProducer kafkaPaymentProducer;

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

	@KafkaListener(topics = "payment-create", groupId = "payment-service")
	@Transactional
	public void handleCreatePayment(PaymentCreateEvent event) {
		log.info("📥 결제 생성 이벤트 수신: {}", event);

		Payment payment = Payment.toReadyEntity(event);
		paymentDomainService.saveReadyPayment(payment);

		try {
			// TODO: 실제 할인 적용은 coupon-service 연동 필요 (지금은 mock)
			if (event.getCouponId() != null) {
				int discountAmount = 10; // 가상 데이터
				DiscountType discountType = DiscountType.RATE;
				int finalAmount = paymentDomainService.calculateDiscount(
					event.getOriginalAmount(), discountAmount, discountType
				);
				payment.applyDiscount(discountAmount, finalAmount);
			}

			payment.successPayment();
			paymentRepository.save(payment);

			kafkaPaymentProducer.sendSuccessEvent(PaymentSuccessEvent.toDto(payment));

		} catch (Exception e) {
			log.error("❌ 결제 처리 실패: {}", e.getMessage());
			payment.failPayment();
			paymentRepository.save(payment);

			kafkaPaymentProducer.sendFailedEvent(PaymentFailedEvent.toDto(payment));
		}
	}
}
