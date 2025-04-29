package com.tweaty.reservation.application;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReservationCacheService {

	private final RedisTemplate<String, Object> redisTemplate;

	public ReservationCacheService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void cacheReservation(String key, Object value, long ttlMinutes) {
		redisTemplate.opsForValue().set(key, value, ttlMinutes, TimeUnit.MINUTES);
	}

	public Object getFromCache(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void removeFromCache(String key) {
		redisTemplate.delete(key);
	}
}