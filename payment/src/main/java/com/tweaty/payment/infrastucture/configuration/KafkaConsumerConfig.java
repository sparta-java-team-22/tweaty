package com.tweaty.payment.infrastucture.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

	@Bean
	public DefaultErrorHandler errorHandler() {
		// 최대 3번까지 재시도, 1초 간격
		FixedBackOff fixedBackOff = new FixedBackOff(1000L, 1);

		DefaultErrorHandler handler = new DefaultErrorHandler((record, ex) -> {
			System.err.println("처리 실패 메시지: " + record);
			ex.printStackTrace();
		}, fixedBackOff);

		return handler;
	}
}
