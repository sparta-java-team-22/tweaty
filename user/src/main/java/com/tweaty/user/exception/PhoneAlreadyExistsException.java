package com.tweaty.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class PhoneAlreadyExistsException extends CustomException {
	public PhoneAlreadyExistsException() {
		super(ErrorCode.PHONE_ALREADY_EXISTS, HttpStatus.CONFLICT);
	}
}
