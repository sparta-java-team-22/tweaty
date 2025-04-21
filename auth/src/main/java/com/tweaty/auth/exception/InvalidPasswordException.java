package com.tweaty.auth.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class InvalidPasswordException extends CustomException {
	public InvalidPasswordException() {
		super(ErrorCode.INVALID_PASSWORD, HttpStatus.UNAUTHORIZED);
	}
}
