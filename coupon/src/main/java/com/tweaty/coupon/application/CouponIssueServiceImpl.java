package com.tweaty.coupon.application;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.coupon.application.dto.CouponIssueResponse;
import com.tweaty.coupon.application.dto.CouponIssueStatus;
import com.tweaty.coupon.application.dto.CouponIssueStatusResponse;
import com.tweaty.coupon.domain.model.Coupon;
import com.tweaty.coupon.domain.model.CouponIssue;
import com.tweaty.coupon.domain.repository.CouponCacheRepository;
import com.tweaty.coupon.domain.repository.CouponIssueRepository;
import com.tweaty.coupon.domain.repository.CouponRepository;
import com.tweaty.coupon.domain.vo.CouponIssuancePeriod;
import com.tweaty.coupon.domain.vo.Quantity;
import com.tweaty.coupon.exception.CouponAlreadyIssuedException;
import com.tweaty.coupon.exception.CouponExpiredException;
import com.tweaty.coupon.exception.CouponNotFoundException;
import com.tweaty.coupon.exception.CouponOutOfStockException;
import com.tweaty.coupon.infrastructure.kafka.producer.CouponStockProducer;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueServiceImpl implements CouponIssueService {
	private static final int ALREADY_ISSUED_RESULT = -1;
	private static final int COUPON_OUT_OF_STOCK_RESULT = -2;
	private final CouponRepository couponRepository;
	private final CouponIssueRepository couponIssueRepository;
	private final CouponCacheRepository couponCacheRepository;
	private final CouponStockProducer couponStockProducer;

	@Override
	@Transactional
	public CouponIssueResponse issueCouponV1WithLock(UUID couponId, UUID customerId) {
		// 쿠폰 유효성 검사
		Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId);
		log.info("📍쿠폰 ID: {}, 원래 재고: {}, 발급 전 남은 재고: {}",
			coupon.getCouponId(),
			coupon.getCouponMaxIssuance().getValue(),
			coupon.getCouponRemainingStock().getValue()
		);

		LocalDateTime currentTime = LocalDateTime.now();

		checkCouponPeriod(coupon, currentTime);
		// lock 걸린 coupon 사용
		checkCouponStock(coupon);

		// 중복 발급 검사
		checkAlreadyIssued(couponId, customerId);

		// 재고 차감
		coupon.updateCouponRemainingStock();
		log.info("📍쿠폰 ID: {}, 원래 재고: {}, 발급 전 남은 재고: {}",
			coupon.getCouponId(),
			coupon.getCouponMaxIssuance().getValue(),
			coupon.getCouponRemainingStock().getValue()
		);

		CouponIssue couponIssue = CouponIssue.create(coupon, customerId, currentTime);
		couponIssueRepository.save(couponIssue);

		return CouponIssueResponse.from(couponIssue, coupon);
	}

	@Override
	@Transactional
	public CouponIssueResponse issueCouponV2WithRedis(UUID couponId, UUID customerId) {
		Coupon coupon = findCoupon(couponId);

		log.info("쿠폰 조회 완료 couponId: {}, customerId: {}", couponId, customerId);

		LocalDateTime currentTime = LocalDateTime.now();
		Duration ttl = Duration.between(currentTime, coupon.getCouponValidUntil());

		Long result = 0L;
		try {
			result = couponCacheRepository.tryIssueCoupon(couponId, customerId, ttl.getSeconds());
		} catch (Exception e) {
			log.error("쿠폰 재고 확인 및 중복 발급 여부 확인 불가: {}", e.getMessage());
			throw new IllegalStateException(e.getMessage());
		}

		checkCouponIssuedAvailable(result);
		couponStockProducer.sendIssueEvent(couponId);

		CouponIssue couponIssue = CouponIssue.create(coupon, customerId, currentTime);
		couponIssueRepository.save(couponIssue);

		return CouponIssueResponse.from(couponIssue, coupon);
	}

	@Override
	@Transactional(readOnly = true)
	public CouponIssueStatusResponse getCouponIssueStatus(UUID couponId, UUID customerId) {
		Optional<CouponIssue> couponIssue = couponIssueRepository.findByCouponIdAndCustomerId(couponId, customerId);

		return couponIssue
			.map(issue -> CouponIssueStatusResponse.from(issue, CouponIssueStatus.SUCCESS))
			.orElseGet(() -> CouponIssueStatusResponse.notIssued(couponId, customerId));
	}

	@Transactional
	public void updateCouponStock(UUID couponId) {
		final Coupon coupon = findCoupon(couponId);
		coupon.updateCouponRemainingStock();
	}

	private Coupon findCoupon(UUID couponId) {
		return couponRepository.findByCouponId(couponId)
			.orElseThrow(() -> new CouponNotFoundException(ErrorCode.COUPON_NOT_FOUND));
	}

	private void checkCouponIssuedAvailable(Long result) {
		if (result == ALREADY_ISSUED_RESULT) {
			throw new CouponAlreadyIssuedException(ErrorCode.COUPON_ALREADY_ISSUED);
		}

		if (result == COUPON_OUT_OF_STOCK_RESULT) {
			throw new CouponOutOfStockException(ErrorCode.COUPON_OUT_OF_STOCK);
		}
	}

	private void checkCouponPeriod(Coupon coupon, LocalDateTime currentTime) {
		CouponIssuancePeriod couponIssuancePeriod = coupon.getCouponIssuancePeriod();

		if (currentTime.isBefore(couponIssuancePeriod.getStartAt()) || currentTime.isAfter(
			couponIssuancePeriod.getEndAt())) {
			throw new CouponExpiredException(ErrorCode.COUPON_EXPIRED);
		}
	}

	private void checkCouponStock(Coupon coupon) {
		Quantity couponRemainingStock = coupon.getCouponRemainingStock();
		if (couponRemainingStock.getValue() <= 0) {
			throw new CouponOutOfStockException(ErrorCode.COUPON_OUT_OF_STOCK);
		}
	}

	private void checkAlreadyIssued(UUID couponId, UUID customerId) {
		if (couponIssueRepository.existsByCouponIdAndCustomerId(couponId, customerId)) {
			throw new CouponAlreadyIssuedException(ErrorCode.COUPON_ALREADY_ISSUED);
		}
	}
}
