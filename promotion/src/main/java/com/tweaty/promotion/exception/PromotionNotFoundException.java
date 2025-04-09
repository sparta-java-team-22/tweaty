package com.tweaty.promotion.exception;

import exception.ErrorCode;

public class PromotionNotFoundException extends CustomException {
	public PromotionNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
