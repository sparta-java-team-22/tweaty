package com.tweaty.coupon.application;

import java.util.UUID;

import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.application.dto.CouponIssueResponse;
import com.tweaty.coupon.application.dto.CouponReadResponse;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;
import com.tweaty.coupon.presentation.request.CouponIssueRequest;

public interface CouponService {
	CouponCreateResponse createCoupon(CouponCreateRequest request);

	CouponIssueResponse issueCoupon(UUID couponId, CouponIssueRequest request);

	CouponReadResponse getCoupon(UUID couponId);
}
