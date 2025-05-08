package com.tweaty.coupon.infrastructure.kafka.consumer;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.tweaty.coupon.application.CouponIssueService;
import com.tweaty.coupon.infrastructure.kafka.event.TimeAttackCouponCreateEvent;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeAttackCouponConsumer {
	private final CouponIssueService couponIssueService;
	private final ObservationRegistry observationRegistry;

	@KafkaListener(
		groupId = "${spring.kafka.groups.coupon-create}",
		topics = "${spring.kafka.topics.coupon-create}",
		containerFactory = "batchKafkaListenerContainerFactory"
	)
	public void consume(List<TimeAttackCouponCreateEvent> events) {
		log.info("Received {} events", events.size());

		Observation.createNotStarted("kafka.batch.processing", observationRegistry)
			.lowCardinalityKeyValue("batch.size", String.valueOf(events.size()))
			.observe(() -> {
				for (TimeAttackCouponCreateEvent event : events) {
					try {
						log.info("Processing - couponId: {}, customerId: {}", event.couponId(), event.userId());
						couponIssueService.issueCouponV2WithRedis(event.couponId(), event.userId());
						log.info("쿠폰 발급 성공 - couponId: {}, customerId: {}", event.couponId(), event.userId());
					} catch (Exception e) {
						log.error("쿠폰 발급 실패 - couponId: {}, customerId: {}, e:{}", event.couponId(), event.userId(),
							e.getMessage());
					}
				}
			});
	}
}
