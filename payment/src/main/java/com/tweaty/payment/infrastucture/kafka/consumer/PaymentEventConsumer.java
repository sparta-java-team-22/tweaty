package com.tweaty.payment.infrastucture.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.payment.domain.entity.DiscountType;
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.domain.service.PaymentDomainService;
import com.tweaty.payment.infrastucture.client.CouponClient;
import com.tweaty.payment.infrastucture.kafka.event.PaymentCreateEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentFailedEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentSuccessEvent;
import com.tweaty.payment.infrastucture.kafka.producer.KafkaPaymentProducer;
import com.tweaty.payment.presentation.dto.response.CouponReadResponse;

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
		log.info("결제 생성 이벤트 수신: {}", event);

		Payment payment = Payment.toReadyEntity(event);
		paymentDomainService.saveReadyPayment(payment);

		try {
			// TODO: 실제 할인 적용은 coupon-service 연동 필요 (지금은 mock)
			if (event.getCouponId() != null) {
				CouponReadResponse coupon = couponClient.getCouponTest(event.getCouponId());
				int finalAmount = paymentDomainService.calculateDiscount(
					event.getOriginalAmount(), coupon.discountAmount(), coupon.discountType()
				);
				payment.applyDiscount(coupon.discountAmount(), finalAmount);
			}

			payment.successPayment();
			paymentRepository.save(payment);
			log.info(" [Kafka 처리 완료] Payment 저장!");

			kafkaPaymentProducer.sendSuccessEvent(PaymentSuccessEvent.toDto(payment));

		} catch (Exception e) {
			log.error(" 결제 실패: {}", e.getMessage());
			payment.failPayment();
			paymentRepository.save(payment);

			kafkaPaymentProducer.sendFailedEvent(PaymentFailedEvent.toDto(payment));
		}
	}
}
