package com.tweaty.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class UsernameAlreadyExistsException extends CustomException {
	public UsernameAlreadyExistsException() {
		super(ErrorCode.USERNAME_ALREADY_EXISTS, HttpStatus.CONFLICT);
	}
}
