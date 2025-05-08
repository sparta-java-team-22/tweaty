package com.tweaty.coupon.presentation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.coupon.application.CouponIssueService;
import com.tweaty.coupon.application.dto.CouponIssueResponse;
import com.tweaty.coupon.application.dto.CouponIssueStatusResponse;

import constant.UserConstant;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponIssueController {
	private final CouponIssueService couponIssueService;

	@PostMapping("/v1/coupons/{couponId}/issue")
	public ResponseEntity<CouponIssueResponse> issueCouponV1(
		@PathVariable UUID couponId,
		@RequestHeader(UserConstant.X_USER_ID) UUID customerId
	) {
		CouponIssueResponse response = couponIssueService.issueCouponV1WithLock(couponId, customerId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/v2/coupons/{couponId}/issue")
	public ResponseEntity<CouponIssueResponse> issueCouponV2(
		@PathVariable UUID couponId,
		@RequestHeader(UserConstant.X_USER_ID) UUID customerId
	) {
		CouponIssueResponse response = couponIssueService.issueCouponV2WithRedis(couponId, customerId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/v2/coupons/{couponId}/status")
	ResponseEntity<CouponIssueStatusResponse> getCouponIssueStatus(
		@PathVariable UUID couponId,
		@RequestHeader(UserConstant.X_USER_ID) UUID customerId
	) {
		CouponIssueStatusResponse response = couponIssueService.getCouponIssueStatus(couponId, customerId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
