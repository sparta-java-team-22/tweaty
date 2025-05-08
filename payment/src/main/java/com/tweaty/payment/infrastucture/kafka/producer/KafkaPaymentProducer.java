package com.tweaty.payment.infrastucture.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tweaty.payment.infrastucture.kafka.event.PaymentCreateEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentFailedEvent;
import com.tweaty.payment.infrastucture.kafka.event.PaymentSuccessEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPaymentProducer {

	private final KafkaTemplate<String, PaymentSuccessEvent> kafkaSuccessTemplate;
	private final KafkaTemplate<String, PaymentFailedEvent> kafkaFailedTemplate;
	private final KafkaTemplate<String, PaymentCreateEvent> kafkaCreateTemplate;

	public void sendSuccessEvent(PaymentSuccessEvent event) {
		log.info(" Kafka 발행: {}", event);
		kafkaSuccessTemplate.send("payment-success", event);
	}

	public void sendFailedEvent(PaymentFailedEvent event) {
		kafkaFailedTemplate.send("payment-failed", event);
		log.info("PaymentFailedEvent sent to Kafka: {}", event);
	}


	public void sendCreateEventNature(PaymentCreateEvent event) {
		log.info("결제 요청 이벤트 발행됨: {}", event);
		kafkaCreateTemplate.send("payment-create-test", event);
	}


	public void sendCreateEvent(PaymentCreateEvent event) {
		log.info("결제 요청 이벤트 발행됨: {}", event);
		kafkaCreateTemplate.send("payment-create", event);
	}

	public void sendCreateEvenByRedisson(PaymentCreateEvent event) {
		log.info("결제 요청 이벤트 발행됨: {}", event);
		kafkaCreateTemplate.send("payment-create-redisson", event);
	}

}