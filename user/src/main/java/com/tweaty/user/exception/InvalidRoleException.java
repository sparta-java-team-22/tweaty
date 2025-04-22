package com.tweaty.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class InvalidRoleException extends CustomException {
	public InvalidRoleException() {
		super(ErrorCode.INVALID_ROLE, HttpStatus.BAD_REQUEST);
	}
}
