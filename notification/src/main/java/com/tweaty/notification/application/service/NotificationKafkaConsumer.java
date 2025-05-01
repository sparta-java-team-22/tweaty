package com.tweaty.notification.application.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tweaty.notification.application.dto.NotificationRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaConsumer {

	private final NotificationService notificationService;

	/**
	 * Consumes signup notification messages from the "notification-topic" Kafka topic and delegates processing to the notification service.
	 *
	 * @param requestDto the notification data received from Kafka
	 */
	@KafkaListener(topics = "notification-topic", groupId = "notification-service-group")
	public void consumeSignupNotification(NotificationRequestDto requestDto) {

		log.info("Received signup notification: {}", requestDto);
		notificationService.createSignupNotification(requestDto);

	}

}
