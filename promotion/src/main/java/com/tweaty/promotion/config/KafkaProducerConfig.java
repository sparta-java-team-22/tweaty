package com.tweaty.promotion.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.tweaty.promotion.infrastructure.kafka.event.TimeAttackCouponCreateEvent;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	
	@Bean
	public ProducerFactory<String, TimeAttackCouponCreateEvent> producerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, TimeAttackCouponCreateEvent> kafkaTemplate() {
		KafkaTemplate<String, TimeAttackCouponCreateEvent> kafkaTemplate = new KafkaTemplate<>(producerFactory());
		kafkaTemplate.setObservationEnabled(true);
		return kafkaTemplate;
	}
}
