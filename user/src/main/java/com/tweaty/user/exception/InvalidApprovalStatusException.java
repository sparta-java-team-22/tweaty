package com.tweaty.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class InvalidApprovalStatusException extends CustomException {
	public InvalidApprovalStatusException() {
		super(ErrorCode.INVALID_APPROVAL_STATUS, HttpStatus.BAD_REQUEST);
	}
}
