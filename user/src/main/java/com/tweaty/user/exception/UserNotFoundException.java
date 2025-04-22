package com.tweaty.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class UserNotFoundException extends CustomException {
	public UserNotFoundException () {
		super(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
	}
}
