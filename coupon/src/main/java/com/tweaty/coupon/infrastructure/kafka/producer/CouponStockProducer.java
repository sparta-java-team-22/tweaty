package com.tweaty.coupon.infrastructure.kafka.producer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponStockProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;
	@Value("${spring.kafka.topics.coupon-issued}")
	private String couponIssued;

	public void sendIssueEvent(UUID couponId) {
		String message = couponId.toString();
		kafkaTemplate.send(couponIssued, message);
	}
}
