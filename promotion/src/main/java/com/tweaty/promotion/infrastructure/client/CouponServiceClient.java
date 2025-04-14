package com.tweaty.promotion.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tweaty.promotion.infrastructure.client.dto.CouponIssueRequest;
import com.tweaty.promotion.infrastructure.client.dto.CouponIssueResponse;

@FeignClient(name = "coupon-service")
public interface CouponServiceClient {
	@PostMapping("/api/v1/coupons/{couponId}/issue")
	CouponIssueResponse issueCoupon(@PathVariable("couponId") UUID couponId, @RequestBody CouponIssueRequest request);
}
