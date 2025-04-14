package com.tweaty.coupon.presentation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.coupon.application.CouponService;
import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.application.dto.CouponIssueResponse;
import com.tweaty.coupon.application.dto.CouponReadResponse;
import com.tweaty.coupon.application.dto.CouponUpdateResponse;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;
import com.tweaty.coupon.presentation.request.CouponIssueRequest;
import com.tweaty.coupon.presentation.request.CouponUpdateRequest;

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

	@PatchMapping("/{couponId}")
	public ResponseEntity<CouponUpdateResponse> updateCoupon(
		@PathVariable UUID couponId,
		@RequestBody CouponUpdateRequest request
	) {
		CouponUpdateResponse response = couponService.updateCoupon(couponId, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}
}
