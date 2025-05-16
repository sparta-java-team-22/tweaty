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

	@KafkaListener(topics = "refund-success", groupId = "refund-consumer")
	public void consumePaymentSuccess(RefundSuccessEvent event) {
		log.info("환불 성공 이벤트 수신: {}", event);

		// 여기서 알림 전송, 포인트 적립 등 처리
	}

	@KafkaListener(topics = "refund-failed", groupId = "alert-service")
	public void consumePaymentFailed(RefundFailedEvent event) {
		log.warn("환불 실패 이벤트 수신: {}", event);
		// 알림 전송, 로그 저장, 슬랙 메시지 발송 등 처리 가능
	}

	@KafkaListener(topics = "refund-create", groupId = "refund-service")
	@Transactional
	public void handleRefundCreatePayment(RefundCreateEvent event) {
		log.info("환불 생성 이벤트 수신: {}", event);

		Refund refund = null;
		Payment payment = null;

		try {
			payment = paymentDomainService.findPayment(event.getPaymentId());
			refund = paymentDomainService.findRefund(event.getRefundId(), event.getUserId());

			if (payment.getCouponId() != null) {
				// TODO: 쿠폰 복원 로직 (예: couponClient.restoreCoupon(...))
			}

			refund.successRefund();
			payment.successRefund();
			refundRepository.save(refund);
			log.info(" [Kafka 처리 완료] Refund 저장");

			kafkaRefundProducer.sendSuccessEvent(RefundSuccessEvent.toDto(refund));

		} catch (Exception e) {
			log.error("환불 실패: {}", e.getMessage());

			// 실패 처리 보장
			if (refund != null) {
				refund.failRefund();
				refundRepository.save(refund);

				kafkaRefundProducer.sendFailedEvent(RefundFailedEvent.toDto(refund));
			} else {
				log.warn("Refund 객체 없음. 실패 상태 저장 및 이벤트 발행 불가");
			}

			// 예외 던져서 Kafka 재시도하게 할지 말지는 설정에 따라 결정됨
			throw new CustomException(ErrorCode.REFUND_FAIL, HttpStatus.BAD_REQUEST);
		}
	}

}