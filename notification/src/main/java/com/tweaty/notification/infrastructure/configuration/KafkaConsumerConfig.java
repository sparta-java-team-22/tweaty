package com.tweaty.notification.infrastructure.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	/**
	 * Creates and configures a Kafka {@link ConsumerFactory} for consuming messages with String keys and JSON values.
	 *
	 * <p>
	 * The consumer factory is set up to connect to a local Kafka broker, disables automatic offset commits,
	 * and uses {@link StringDeserializer} for keys. For values, it uses {@link ErrorHandlingDeserializer}
	 * wrapping a {@link JsonDeserializer}, enabling robust handling of JSON payloads and deserialization errors.
	 * Type mappings are provided to map notification DTOs from different packages to a unified DTO class,
	 * and all packages are trusted for deserialization.
	 * </p>
	 *
	 * @return a configured {@link ConsumerFactory} for Kafka consumers handling notification messages
	 */
	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {

		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
		configProps.put(JsonDeserializer.TYPE_MAPPINGS,
			"com.tweaty.auth.application.dto.NotificationRequestDto:com.tweaty.notification.application.dto.NotificationRequestDto," +
				"com.tweaty.user.application.dto.NotificationRequestDto:com.tweaty.notification.application.dto.NotificationRequestDto");
		configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return new DefaultKafkaConsumerFactory<>(configProps);

	}

	/**
	 * Creates and configures a Kafka listener container factory for concurrent message consumption.
	 *
	 * @return a ConcurrentKafkaListenerContainerFactory using the configured consumer factory
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

}
