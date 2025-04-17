package com.tweaty.payment.infrastucture.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.tweaty.payment.infrastucture.kafka.event.PaymentFailedEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentSuccessEvent;
import com.tweaty.payment.infrastucture.kafka.event.RefundCreateEvent;
import com.tweaty.payment.infrastucture.kafka.event.RefundFailedEvent;
import com.tweaty.payment.infrastucture.kafka.event.RefundSuccessEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaRefundProducer {

	private final KafkaTemplate<String, RefundCreateEvent> kafkaRefundCreateTemplate;
	private final KafkaTemplate<String, RefundSuccessEvent> kafkaRefundSuccessTemplate;
	private final KafkaTemplate<String, RefundFailedEvent> kafkaRefundFailedTemplate;



	public void sendSuccessEvent(RefundSuccessEvent event) {
		log.info(" Kafka 발행: {}", event);
		kafkaRefundSuccessTemplate.send("refund-success", event);
	}

	public void sendFailedEvent(RefundFailedEvent event) {
		kafkaRefundFailedTemplate.send("payment-failed", event);
		log.info("PaymentFailedEvent sent to Kafka: {}", event);
	}


	public void sendRefundEvent(RefundCreateEvent event) {
		log.info("환불 요청 이벤트 발행됨: {}", event);
		kafkaRefundCreateTemplate.send("refund-create", event);
	}
}