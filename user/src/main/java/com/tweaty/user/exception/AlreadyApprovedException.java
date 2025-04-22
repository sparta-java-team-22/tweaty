package com.tweaty.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class AlreadyApprovedException extends CustomException {
	public AlreadyApprovedException() {
		super(ErrorCode.ALREADY_APPROVED, HttpStatus.BAD_REQUEST);
	}
}
