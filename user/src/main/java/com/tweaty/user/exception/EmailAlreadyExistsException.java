package com.tweaty.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class EmailAlreadyExistsException extends CustomException {
	public EmailAlreadyExistsException() {
		super(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
	}
}
