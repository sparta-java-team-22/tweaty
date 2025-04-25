package com.tweaty.promotion.infrastructure.redis;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweaty.promotion.domain.model.Promotion;
import com.tweaty.promotion.exception.PromotionNotFoundException;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisPromotionRepository {
	private static final String CACHE_KEY_PREFIX = "promotion:";
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public void cachePromotion(Promotion promotion) {
		String key = generatePromotionKey(promotion.getPromotionId());
		log.info("redis key: {}", key);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endAt = promotion.getEventPeriod().getEndAt();

		Duration ttl = Duration.between(now, endAt);

		if (ttl.isNegative() || ttl.isZero()) {
			ttl = Duration.ofMinutes(1);
		}

		redisTemplate.opsForValue()
			.set(key, promotion, ttl);
	}

	public Promotion getPromotion(UUID promotionId) {
		log.info("[Request] promotionId: {}", promotionId);

		String key = generatePromotionKey(promotionId);
		log.info("[Cache] key: {}", key);

		Object raw = redisTemplate.opsForValue().get(key);

		if (raw == null) {
			log.error("[Cache miss] promotionId: {}", promotionId);
			throw new PromotionNotFoundException(ErrorCode.PROMOTION_NOT_FOUND);
		}

		return objectMapper.convertValue(raw, Promotion.class);
	}

	private String generatePromotionKey(UUID promotionId) {
		return CACHE_KEY_PREFIX + promotionId;
	}
}
