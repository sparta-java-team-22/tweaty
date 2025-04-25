package com.tweaty.promotion.application.dto;

public record PromotionTimeAttackCouponAsyncResponse(
	PromotionStatus promotionStatus,
	String promotionStatusMessage
) {
	public static PromotionTimeAttackCouponAsyncResponse from(PromotionStatus promotionStatus) {
		return new PromotionTimeAttackCouponAsyncResponse(
			promotionStatus,
			promotionStatus.getMessage()
		);
	}
}
