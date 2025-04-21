package com.tweaty.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class UserForbiddenException extends CustomException {
	public UserForbiddenException() {
		super(ErrorCode.USER_FORBIDDEN, HttpStatus.FORBIDDEN);
	}
}
