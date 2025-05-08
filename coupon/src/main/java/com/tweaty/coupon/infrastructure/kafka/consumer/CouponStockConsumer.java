package com.tweaty.coupon.infrastructure.kafka.consumer;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.tweaty.coupon.application.CouponIssueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponStockConsumer {

	private final CouponIssueService couponIssueService;

	@KafkaListener(topics = "${spring.kafka.topics.coupon-issued}", groupId = "${spring.kafka.groups.coupon-issued}")
	public void consume(String message) {
		UUID couponId = UUID.fromString(message);

		log.info("쿠폰 발급 처리 시작: couponId={}", couponId);

		couponIssueService.updateCouponStock(couponId);

		log.info("쿠폰 발급 처리 완료: couponId={}", couponId);
	}
}
