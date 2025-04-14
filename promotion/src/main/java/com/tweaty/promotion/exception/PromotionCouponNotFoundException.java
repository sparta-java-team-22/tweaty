package com.tweaty.promotion.exception;

import exception.ErrorCode;

public class PromotionCouponNotFoundException extends CustomException {
	public PromotionCouponNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
