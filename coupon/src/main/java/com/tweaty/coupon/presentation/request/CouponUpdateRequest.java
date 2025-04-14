package com.tweaty.coupon.presentation.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponUpdateRequest {
	private String couponName;
	private String discountType;
	private Integer discountAmount;
	private Integer couponMaxIssuance;
	private LocalDateTime couponIssuanceStartAt;
	private LocalDateTime couponIssuanceEndAt;
	private Boolean isFirstCome;
}
