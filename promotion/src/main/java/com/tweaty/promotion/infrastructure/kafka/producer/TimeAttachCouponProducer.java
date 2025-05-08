package com.tweaty.promotion.infrastructure.kafka.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.tweaty.promotion.infrastructure.kafka.event.TimeAttackCouponCreateEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeAttachCouponProducer {
	private final KafkaTemplate<String, TimeAttackCouponCreateEvent> timeAttackCouponCreateEventKafkaTemplate;

	@Value("${spring.kafka.topics.coupon-create}")
	private String couponCreateTopic;

	public void sendTimeAttackCouponCreateEvent(TimeAttackCouponCreateEvent event) {
		log.info("🔥선착순 쿠폰 발급 이벤트 발행 시작: 쿠폰 ID: {}, 사용자 ID: {}", event.couponId(), event.userId());

		timeAttackCouponCreateEventKafkaTemplate.send(couponCreateTopic, event.couponId().toString(), event)
			.whenComplete((result, ex) -> {
				if (ex == null)
					handleSuccess(event);
				else
					handleFailure(event, ex);
			});
	}

	private void handleSuccess(TimeAttackCouponCreateEvent event) {
		log.info("✅선착순 쿠폰 발급 이벤트 발행 성공! 쿠폰 ID: {}, 사용자 ID: {}", event.couponId(), event.userId());
	}

	private void handleFailure(TimeAttackCouponCreateEvent event, Throwable ex) {
		log.error("🐞선착순 쿠폰 발급 이벤트 발행 실패! 쿠폰 ID: {}, 사용자 ID: {}", event.couponId(), event.userId());
		log.error("🐞실패 메시지: {}", ex.getMessage());
	}
}
