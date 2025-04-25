package com.tweaty.promotion.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.promotion.application.dto.PromotionCreateResponse;
import com.tweaty.promotion.application.dto.PromotionReadResponse;
import com.tweaty.promotion.application.dto.PromotionStatus;
import com.tweaty.promotion.application.dto.PromotionTimeAttackCouponAsyncResponse;
import com.tweaty.promotion.application.dto.PromotionTimeAttackCouponSyncResponse;
import com.tweaty.promotion.domain.model.Promotion;
import com.tweaty.promotion.domain.repository.PromotionRepository;
import com.tweaty.promotion.exception.IssueCouponApiFailException;
import com.tweaty.promotion.exception.PromotionCouponNotFoundException;
import com.tweaty.promotion.exception.PromotionDuplicatedException;
import com.tweaty.promotion.infrastructure.client.CouponServiceClient;
import com.tweaty.promotion.infrastructure.client.dto.CouponIssueRequest;
import com.tweaty.promotion.infrastructure.client.dto.CouponIssueResponse;
import com.tweaty.promotion.infrastructure.kafka.event.TimeAttackCouponCreateEvent;
import com.tweaty.promotion.infrastructure.kafka.producer.TimeAttachCouponProducer;
import com.tweaty.promotion.infrastructure.redis.RedisPromotionRepository;
import com.tweaty.promotion.presentation.request.PromotionCreateRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
	private final PromotionRepository promotionRepository;
	private final CouponServiceClient couponServiceClient;
	private final TimeAttachCouponProducer timeAttachCouponProducer;
	private final RedisPromotionRepository redisPromotionRepository;

	@Override
	@Transactional
	public PromotionCreateResponse createEvent(PromotionCreateRequest request) {
		// 쿠폰 존재 여부 확인
		if (promotionRepository.existsByEventName(request.eventName())) {
			throw new PromotionDuplicatedException(ErrorCode.PROMOTION_DUPLICATED);
		}

		Promotion promotion = Promotion.create(request);
		promotionRepository.save(promotion);

		log.info("캐시 저장 직전 promotion id: {}", promotion.getPromotionId());
		redisPromotionRepository.cachePromotion(promotion);

		return PromotionCreateResponse.from(promotion);
	}

	@Override
	@Transactional(readOnly = true)
	public PromotionReadResponse getEvent(UUID eventId) {
		Promotion promotion = getPromotion(eventId);

		return PromotionReadResponse.from(promotion);
	}

	@Override
	@Transactional
	public void updateEventStatusToEnded(UUID eventId) {
		Promotion promotion = getPromotion(eventId);

		promotion.updateEventStatusToEnded();
	}

	@Override
	@Transactional
	public PromotionTimeAttackCouponSyncResponse issueTimeAttackCouponV1Sync(UUID eventId) {
		// 이벤트 유효성 확인
		Promotion promotion = getPromotion(eventId);
		checkEventPeriod(promotion);

		// 쿠폰 유효성 확인
		checkCouponAvailable(promotion);

		// 쿠폰 발급
		CouponIssueResponse couponIssueResponse = issueCoupon(promotion);

		return PromotionTimeAttackCouponSyncResponse.from(couponIssueResponse);
	}

	@Override
	@Transactional
	public PromotionTimeAttackCouponAsyncResponse issueTimeAttackCouponV2Async(UUID eventId, UUID userId) {
		Promotion promotion = redisPromotionRepository.getPromotion(eventId);

		checkEventPeriod(promotion);
		checkCouponAvailable(promotion);

		try {
			timeAttachCouponProducer.sendTimeAttackCouponCreateEvent(
				TimeAttackCouponCreateEvent.from(
					promotion.getCouponId(),
					userId
				)
			);
			return PromotionTimeAttackCouponAsyncResponse.from(PromotionStatus.PENDING);
		} catch (Exception e) {
			log.error("🔥쿠폰 발급 요청 실패: {}", e.getMessage());
			return PromotionTimeAttackCouponAsyncResponse.from(PromotionStatus.FAILED);
		}
	}

	private Promotion getPromotion(UUID eventId) {
		Promotion promotion = redisPromotionRepository.getPromotion(eventId);

		if (promotion == null) {
			return getPromotion(eventId);
		}

		return promotion;
	}

	private void checkEventPeriod(Promotion promotion) {
		LocalDateTime now = LocalDateTime.now();
		promotion.checkEventPeriod(now);
	}

	private void checkCouponAvailable(Promotion promotion) {
		if (promotion.getCouponId() == null) {
			throw new PromotionCouponNotFoundException(ErrorCode.PROMOTION_COUPON_NOT_FOUND);
		}
	}

	private CouponIssueResponse issueCoupon(Promotion promotion) {
		try {
			log.info("요청 시작!");
			CouponIssueResponse response = couponServiceClient.issueCoupon(
				promotion.getCouponId(),
				CouponIssueRequest.from(promotion)
			);
			log.info("쿠폰 발급 api 요청 완료");
			return response;
		} catch (Exception e) {
			log.error("쿠폰 발급 api 요청 실패: {}", e.getMessage());
			throw new IssueCouponApiFailException(ErrorCode.PROMOTION_ISSUE_COUPON_API_FAIL);
		}
	}
}
