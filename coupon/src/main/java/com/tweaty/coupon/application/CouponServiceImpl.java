package com.tweaty.coupon.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.application.dto.CouponReadResponse;
import com.tweaty.coupon.application.dto.CouponStatusUpdateResponse;
import com.tweaty.coupon.application.dto.CouponUpdateResponse;
import com.tweaty.coupon.domain.model.Coupon;
import com.tweaty.coupon.domain.model.CouponIssue;
import com.tweaty.coupon.domain.repository.CouponCacheRepository;
import com.tweaty.coupon.domain.repository.CouponIssueRepository;
import com.tweaty.coupon.domain.repository.CouponRepository;
import com.tweaty.coupon.exception.CouponNotFoundException;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;
import com.tweaty.coupon.presentation.request.CouponUpdateRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
	private final CouponRepository couponRepository;
	private final CouponIssueRepository couponIssueRepository;
	private final CouponCacheRepository couponCacheRepository;

	@Override
	@Transactional
	public CouponCreateResponse createCoupon(CouponCreateRequest request) {
		Coupon coupon = Coupon.create(request);
		couponRepository.save(coupon);

		couponCacheRepository.cacheCoupon(coupon);

		return CouponCreateResponse.from(coupon);
	}

	@Override
	@Transactional(readOnly = true)
	public CouponReadResponse getCoupon(UUID couponId) {
		Coupon coupon = findCoupon(couponId);
		return CouponReadResponse.from(coupon);
	}

	@Override
	@Transactional
	public CouponUpdateResponse updateCoupon(UUID couponId, CouponUpdateRequest request) {
		Coupon coupon = findCoupon(couponId);
		coupon.updateCoupon(request);
		return CouponUpdateResponse.from(coupon);
	}

	@Override
	@Transactional
	public void deleteCoupon(UUID couponId) {
		Coupon coupon = findCoupon(couponId);
		coupon.softDelete();
		log.info("[쿠폰 삭제 완료] couponId:{}", couponId);
	}

	@Override
	@Transactional
	public CouponStatusUpdateResponse useCoupon(
		UUID couponId,
		UUID customerId
	) {
		CouponIssue couponIssue = findCouponIssue(couponId, customerId);

		couponIssue.updateUsedCouponStatus();

		return CouponStatusUpdateResponse.from(couponIssue);
	}

	@Override
	@Transactional
	public CouponStatusUpdateResponse cancelCoupon(UUID couponId, UUID customerId) {
		CouponIssue couponIssue = findCouponIssue(couponId, customerId);

		couponIssue.updateUnusedCouponStatus();

		return CouponStatusUpdateResponse.from(couponIssue);
	}

	private Coupon findCoupon(UUID couponId) {
		return couponRepository.findByCouponId(couponId)
			.orElseThrow(() -> new CouponNotFoundException(ErrorCode.COUPON_NOT_FOUND));
	}

	private CouponIssue findCouponIssue(UUID couponId, UUID customerId) {
		return couponIssueRepository.findByCouponIdAndCustomerId(couponId, customerId)
			.orElseThrow(() -> new CouponNotFoundException(ErrorCode.COUPON_ISSUE_NOT_FOUND));
	}
}
