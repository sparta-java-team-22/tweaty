package com.tweaty.promotion.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.topics.coupon-create}")
	private String couponCreateTopic;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> config = new HashMap<>();
		config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		return new KafkaAdmin(config);
	}

	@Bean
	public NewTopic timeAttackCouponCreateTopic() {
		return TopicBuilder.name(couponCreateTopic)
			.partitions(3)
			.replicas(1)
			.config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "1")
			.build();
	}
}

