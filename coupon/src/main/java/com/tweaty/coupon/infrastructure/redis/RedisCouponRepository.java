package com.tweaty.coupon.infrastructure.redis;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import com.tweaty.coupon.domain.model.Coupon;
import com.tweaty.coupon.domain.repository.CouponCacheRepository;
import com.tweaty.coupon.domain.vo.CouponIssuancePeriod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCouponRepository implements CouponCacheRepository {
	private static final String COUPON_STOCK_KEY = "coupon:stock:";
	private static final String COUPON_ISSUED_KEY = "coupon:issued:";
	private static final String COUPON_CACHED_KEY = "coupon:";
	private static final String ISSUE_COUPON_LUA = """
		      if redis.call('SISMEMBER', KEYS[2], ARGV[2]) == 1 then
		          return -1
		      end
		      local stock = tonumber(redis.call('GET', KEYS[1]))
		      if not stock or stock <= 0 then
		          return -2
		      end
		      redis.call('DECR', KEYS[1])
		      redis.call('SADD', KEYS[2], ARGV[2])
		      if redis.call('TTL', KEYS[2]) == -1 then
		          redis.call('EXPIRE', KEYS[2], ARGV[1])
		      end
		      return 1
		""";

	private final RedisTemplate<String, String> stringRedisTemplate;

	@Override
	public Long tryIssueCoupon(UUID couponId, UUID customerId, Long issuedKeyTtlSeconds) {
		List<String> keys = List.of(
			getStockKey(couponId), // KEYS[1] - 재고 키
			getIssuedKey(couponId)  // KEYS[2] - 발급 이력 키
		);

		List<String> args = List.of(
			String.valueOf(issuedKeyTtlSeconds), // ARGV[1] - 발급 이력 TTL
			customerId.toString() //ARGV[2] - 고객 ID
		);

		Long result = stringRedisTemplate.execute(
			new DefaultRedisScript<>(ISSUE_COUPON_LUA, Long.class),
			keys,
			args.toArray(new String[0])
		);

		log.debug("Lua 스크립트 실행 결과: {}", result);

		if (result == null) {
			log.error("쿠폰 발급 Lua 스크립트 실행 실패! couponId={}, customerId={}", couponId, customerId);
			throw new IllegalStateException("Lua Script 실행 실패!");
		}

		return result;
	}

	@Override
	public void cacheCoupon(Coupon coupon) {
		String stockKey = getStockKey(coupon.getCouponId());

		stringRedisTemplate.opsForValue()
			.set(stockKey, String.valueOf(coupon.getCouponRemainingStock().getValue()));

		Duration ttl = calculateTtl(coupon.getCouponIssuancePeriod());
		stringRedisTemplate.expire(stockKey, ttl);

		log.info("쿠폰 재고 캐싱 완료: {}, 재고: {}", coupon.getCouponId(), coupon.getCouponRemainingStock().getValue());
	}

	private Duration calculateTtl(CouponIssuancePeriod period) {
		return Duration.between(LocalDateTime.now(), period.getEndAt());
	}

	private String getStockKey(UUID couponId) {
		return COUPON_STOCK_KEY + couponId;
	}

	private String getIssuedKey(UUID couponId) {
		return COUPON_ISSUED_KEY + couponId;
	}

	private String getCouponKey(UUID couponId) {
		return COUPON_CACHED_KEY + couponId;
	}
}
