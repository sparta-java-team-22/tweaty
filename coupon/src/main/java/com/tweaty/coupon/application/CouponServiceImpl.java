package com.tweaty.coupon.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.application.dto.CouponIssueResponse;
import com.tweaty.coupon.application.dto.CouponReadResponse;
import com.tweaty.coupon.application.dto.CouponUpdateResponse;
import com.tweaty.coupon.domain.model.Coupon;
import com.tweaty.coupon.domain.model.CouponIssue;
import com.tweaty.coupon.domain.repository.CouponIssueRepository;
import com.tweaty.coupon.domain.repository.CouponRepository;
import com.tweaty.coupon.domain.vo.CouponIssuancePeriod;
import com.tweaty.coupon.domain.vo.Quantity;
import com.tweaty.coupon.exception.CouponAlreadyIssuedException;
import com.tweaty.coupon.exception.CouponExpiredException;
import com.tweaty.coupon.exception.CouponNotFoundException;
import com.tweaty.coupon.exception.CouponOutOfStockException;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;
import com.tweaty.coupon.presentation.request.CouponIssueRequest;
import com.tweaty.coupon.presentation.request.CouponUpdateRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
	private final CouponRepository couponRepository;
	private final CouponIssueRepository couponIssueRepository;

	@Override
	@Transactional
	public CouponCreateResponse createCoupon(CouponCreateRequest request) {

		Coupon coupon = Coupon.create(request);

		couponRepository.save(coupon);

		return CouponCreateResponse.from(coupon);
	}

	@Override
	@Transactional
	public CouponIssueResponse issueCoupon(
		UUID couponId,
		//UUID customerId,
		CouponIssueRequest request
	) {
		// 쿠폰 유효성 검사
		Coupon coupon = couponRepository.findByCouponIdWithLock(couponId);
		checkCouponPeriod(coupon);
		// lock 걸린 coupon 사용
		checkCouponStock(coupon);

		// 중복 발급 검사
		//checkAlreadyIssued(couponId, customerId);

		// 재고 차감 -> todo. 테스트로 동시성 + 재고 차감 되는지 확인
		coupon.updateCouponRemainingStock();

		// CouponIssue Insert
		// todo. customerId 추가 예정
		CouponIssue couponIssue = CouponIssue.create(coupon, request);
		couponIssueRepository.save(couponIssue);

		return CouponIssueResponse.from(couponIssue);
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
	}

	private Coupon findCoupon(UUID couponId) {
		return couponRepository.findByCouponId(couponId)
			.orElseThrow(() -> new CouponNotFoundException(ErrorCode.COUPON_NOT_FOUND));
	}

	private void checkCouponPeriod(Coupon coupon) {
		LocalDateTime now = LocalDateTime.now();
		CouponIssuancePeriod couponIssuancePeriod = coupon.getCouponIssuancePeriod();

		if (now.isBefore(couponIssuancePeriod.getStartAt()) || now.isAfter(couponIssuancePeriod.getEndAt())) {
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
