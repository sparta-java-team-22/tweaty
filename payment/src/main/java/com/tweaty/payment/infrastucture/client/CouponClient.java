package com.tweaty.payment.infrastucture.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tweaty.payment.presentation.dto.response.CouponReadResponse;

@FeignClient("coupon")
public interface CouponClient {

	@GetMapping("/api/v1/coupons/{couponId}/test")
	CouponReadResponse getCouponTest(@PathVariable UUID couponId);
}



