package com.tweaty.payment.infrastucture.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweaty.payment.infrastucture.kafka.event.RefundSuccessEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaRefundProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	private static final String TOPIC = "refund-success";

	public void send(RefundSuccessEvent event) {
		try {
			String payload = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(TOPIC, payload);
			log.info("RefundSuccessEvent sent: {}", payload);
		} catch (JsonProcessingException e) {
			log.error("Failed to send RefundSuccessEvent", e);
		}
	}
}