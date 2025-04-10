package com.tweaty.coupon.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.coupon.application.CouponService;
import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {
	private final CouponService couponService;

	@PostMapping()
	public ResponseEntity<CouponCreateResponse> createCoupon(@RequestBody CouponCreateRequest request) {
		CouponCreateResponse response = couponService.createCoupon(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
