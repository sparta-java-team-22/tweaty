package com.tweaty.coupon.exception;

import exception.ErrorCode;

public class CouponNotFoundException extends CustomException {
	public CouponNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
