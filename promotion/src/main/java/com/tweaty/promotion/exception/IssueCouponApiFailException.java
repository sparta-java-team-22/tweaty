package com.tweaty.promotion.exception;

import exception.ErrorCode;

public class IssueCouponApiFailException extends CustomException {
	public IssueCouponApiFailException(ErrorCode errorCode) {
		super(errorCode);
	}
}
