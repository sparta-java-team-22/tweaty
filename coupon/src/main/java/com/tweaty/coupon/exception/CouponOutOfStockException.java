package com.tweaty.coupon.exception;

import exception.ErrorCode;

public class CouponOutOfStockException extends CustomException {
	public CouponOutOfStockException(ErrorCode errorCode) {
		super(errorCode);
	}
}
