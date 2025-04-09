package com.tweaty.coupon.presentation.request;

import java.time.LocalDateTime;

public record CouponCreateRequest(
	String couponName,
	String discountType,
	Integer discountAmount,
	Integer couponMaxIssuance,
	LocalDateTime couponIssuanceStartAt,
	LocalDateTime couponIssuanceEndAt,
	Boolean isFirstCome
) {
}
