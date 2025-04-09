package com.tweaty.promotion.exception;

import exception.ErrorCode;

public class PromotionDuplicatedException extends CustomException {
	public PromotionDuplicatedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
