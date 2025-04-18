package com.tweaty.promotion.exception;

import exception.ErrorCode;

public class PromotionPeriodViolationException extends CustomException {
	public PromotionPeriodViolationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
