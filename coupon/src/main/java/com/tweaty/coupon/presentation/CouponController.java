package com.tweaty.coupon.presentation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.coupon.application.CouponService;
import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.application.dto.CouponReadResponse;
import com.tweaty.coupon.application.dto.CouponStatusUpdateResponse;
import com.tweaty.coupon.application.dto.CouponUpdateResponse;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;
import com.tweaty.coupon.presentation.request.CouponUpdateRequest;

import constant.UserConstant;
import domain.Role;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {
	private final CouponService couponService;

	@PostMapping()
	public ResponseEntity<CouponCreateResponse> createCoupon(
		@RequestBody CouponCreateRequest request,
		@RequestHeader(UserConstant.X_USER_ROLE) Role role
	) {
		Role.checkAdmin(role);

		CouponCreateResponse response = couponService.createCoupon(request);
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
		@RequestHeader(UserConstant.X_USER_ROLE) Role role,
		@RequestBody CouponUpdateRequest request
	) {
		Role.checkAdmin(role);

		CouponUpdateResponse response = couponService.updateCoupon(couponId, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@DeleteMapping("/{couponId}")
	public ResponseEntity<Void> deleteCoupon(
		@PathVariable UUID couponId,
		@RequestHeader(UserConstant.X_USER_ROLE) Role role
	) {
		Role.checkAdmin(role);

		couponService.deleteCoupon(couponId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/{couponId}/use")
	public ResponseEntity<CouponStatusUpdateResponse> useCoupon(
		@PathVariable UUID couponId,
		@RequestHeader(UserConstant.X_USER_ID) UUID customerId
	) {
		CouponStatusUpdateResponse response = couponService.useCoupon(couponId, customerId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@PatchMapping("/{couponId}/cancel")
	public ResponseEntity<CouponStatusUpdateResponse> cancelCoupon(
		@PathVariable UUID couponId,
		@RequestHeader(UserConstant.X_USER_ID) UUID customerId
	) {
		CouponStatusUpdateResponse response = couponService.cancelCoupon(couponId, customerId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}
}
