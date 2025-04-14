package com.tweaty.coupon.application;

import org.springframework.stereotype.Service;

import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.domain.model.Coupon;
import com.tweaty.coupon.domain.repository.CouponRepository;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
	private final CouponRepository couponRepository;

	@Override
	public CouponCreateResponse createCoupon(CouponCreateRequest request) {

		Coupon coupon = Coupon.create(request);

		couponRepository.save(coupon);

		return CouponCreateResponse.from(coupon);
	}
}
