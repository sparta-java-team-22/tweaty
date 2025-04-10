package com.tweaty.coupon.application;

import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;

public interface CouponService {
	CouponCreateResponse createCoupon(CouponCreateRequest request);
}
