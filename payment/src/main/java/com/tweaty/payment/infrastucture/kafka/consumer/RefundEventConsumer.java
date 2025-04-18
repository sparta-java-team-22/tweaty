package com.tweaty.payment.infrastucture.kafka.consumer;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;
import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.domain.repository.RefundRepository;
import com.tweaty.payment.domain.service.PaymentDomainService;
import com.tweaty.payment.global.exception.CustomException;
import com.tweaty.payment.infrastucture.client.CouponClient;
import com.tweaty.payment.infrastucture.kafka.event.PaymentCreateEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentFailedEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentSuccessEvent;
import com.tweaty.payment.infrastucture.kafka.event.RefundCreateEvent;
import com.tweaty.payment.infrastucture.kafka.event.RefundFailedEvent;
import com.tweaty.payment.infrastucture.kafka.event.RefundSuccessEvent;
import com.tweaty.payment.infrastucture.kafka.producer.KafkaPaymentProducer;
import com.tweaty.payment.infrastucture.kafka.producer.KafkaRefundProducer;
import com.tweaty.payment.presentation.dto.response.CouponReadResponse;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RefundEventConsumer {

	private final PaymentDomainService paymentDomainService;
	private final RefundRepository refundRepository;
	private final KafkaRefundProducer kafkaRefundProducer;
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

	@KafkaListener(topics = "refund-create", groupId = "refund-service")
	@Transactional
	public void handleRefundCreatePayment(RefundCreateEvent event) {
		log.info("환불 생성 이벤트 수신: {}", event);

		Payment payment = paymentDomainService.findPayment(event.getPaymentId());
		Refund refund = paymentDomainService.findRefund(event.getRefundId(),event.getUserId());


		try {
			// TODO: 2. coupon-service: 쿠폰아이디가 있으면 요청보내서 사용가능하게 만들기
			if (payment.getCouponId() != null) {

			}
			// 3. 환불 성공 처리
			refund.successRefund();
			payment.successRefund();
			refundRepository.save(refund);
			log.info(" [Kafka 처리 완료] Refund 저장");

			kafkaRefundProducer.sendSuccessEvent(RefundSuccessEvent.toDto(refund));
		} catch (Exception e) {
			log.error("환불 실패 : {}", e.getMessage());
			refund.failRefund();
			refundRepository.save(refund);
			kafkaRefundProducer.sendFailedEvent(RefundFailedEvent.toDto(refund));

			throw new CustomException(ErrorCode.REFUND_FAIL, HttpStatus.BAD_REQUEST);
		}


	}

}