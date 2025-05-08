package com.tweaty.payment.infrastucture.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

// @Configuration
// public class KafkaConsumerConfig {
//
// 	@Bean
// 	public DefaultErrorHandler errorHandler() {
// 		// 최대 3번까지 재시도, 1초 간격
// 		FixedBackOff fixedBackOff = new FixedBackOff(1000L, 1);
//
// 		DefaultErrorHandler handler = new DefaultErrorHandler((record, ex) -> {
// 			System.err.println("처리 실패 메시지: " + record);
// 			ex.printStackTrace();
// 		}, fixedBackOff);
//
// 		return handler;
// 	}
// }
@Configuration
public class KafkaConsumerConfig {


	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "chat-group"); // 컨슈머 그룹 ID
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 최신 메시지부터 소비하거나 가장 처음부터 소비하는 설정
		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory =
			new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(2); // 병렬로 실행될 컨슈머의 수
		factory.setBatchListener(false); // 메시지 일괄 처리 비활성화
		return factory;
	}
}