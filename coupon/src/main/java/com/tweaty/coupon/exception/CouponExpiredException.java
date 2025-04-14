package com.tweaty.coupon.exception;

import exception.ErrorCode;

public class CouponExpiredException extends CustomException {
	public CouponExpiredException(ErrorCode errorCode) {
		super(errorCode);
	}
}
