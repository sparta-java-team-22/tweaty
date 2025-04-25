package com.tweaty.coupon.infrastructure.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import com.tweaty.coupon.infrastructure.kafka.event.TimeAttackCouponCreateEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TimeAttackCouponConsumerConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	@Value("${spring.kafka.groups.coupon-create}")
	private String groupId;

	@Bean
	public ConsumerFactory<String, TimeAttackCouponCreateEvent> consumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

		config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
		config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024 * 1024);
		config.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 2000);

		return new DefaultKafkaConsumerFactory<>(
			config,
			new StringDeserializer(),
			new JsonDeserializer<>(TimeAttackCouponCreateEvent.class)
		);
	}

	@Bean("batchKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, TimeAttackCouponCreateEvent> kafkaListenerContainerFactory(
		KafkaTemplate<String, Object> kafkaTemplate
	) {
		ConcurrentKafkaListenerContainerFactory<String, TimeAttackCouponCreateEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());

		factory.setConcurrency(3);
		factory.setBatchListener(true);
		factory.getContainerProperties().setObservationEnabled(true);

		DefaultErrorHandler errorHandler = new DefaultErrorHandler(
			new DeadLetterPublishingRecoverer(kafkaTemplate),
			new FixedBackOff(1000L, 3)
		);

		factory.setCommonErrorHandler(errorHandler);

		return factory;
	}
}
