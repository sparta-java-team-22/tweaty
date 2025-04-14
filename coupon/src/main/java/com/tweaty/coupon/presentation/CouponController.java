package com.tweaty.coupon.presentation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.coupon.application.CouponService;
import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.application.dto.CouponIssueResponse;
import com.tweaty.coupon.application.dto.CouponReadResponse;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;
import com.tweaty.coupon.presentation.request.CouponIssueRequest;

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

	@PostMapping("/{couponId}/issue")
	public ResponseEntity<CouponIssueResponse> issueCoupon(
		@PathVariable UUID couponId,
		@RequestBody CouponIssueRequest request
	) {
		UUID customerId = UUID.randomUUID();
		//CouponIssueResponse response = couponService.issueCoupon(couponId, customerId, request);
		CouponIssueResponse response = couponService.issueCoupon(couponId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{couponId}")
	public ResponseEntity<CouponReadResponse> getCoupon(@PathVariable UUID couponId) {
		CouponReadResponse response = couponService.getCoupon(couponId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
