package com.tweaty.coupon.exception;

import exception.ErrorCode;

public class CouponAlreadyIssuedException extends CustomException {
	public CouponAlreadyIssuedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
