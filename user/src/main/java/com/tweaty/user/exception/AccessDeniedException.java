package com.tweaty.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class AccessDeniedException extends CustomException {
	public AccessDeniedException() {
		super(ErrorCode.ACCESS_DENIED, HttpStatus.FORBIDDEN);
	}
}
