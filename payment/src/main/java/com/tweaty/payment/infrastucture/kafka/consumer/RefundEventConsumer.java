package com.tweaty.payment.infrastucture.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RefundEventConsumer {

	@KafkaListener(topics = "refund-success", groupId = "coupon-service")
	public void consume(String message) {
		log.info("📥 Received RefundSuccessEvent: {}", message);
	}
}