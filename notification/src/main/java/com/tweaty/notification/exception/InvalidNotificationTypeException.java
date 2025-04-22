package com.tweaty.notification.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class InvalidNotificationTypeException extends CustomException {
	public InvalidNotificationTypeException() {
		super(ErrorCode.INVALID_NOTIFICATION_TYPE, HttpStatus.BAD_REQUEST);
	}
}
