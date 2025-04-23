package com.tweaty.notification.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class InvalidNotificationChannelException extends CustomException {
	public InvalidNotificationChannelException() {
		super(ErrorCode.INVALID_NOTIFICATION_CHANNEL, HttpStatus.BAD_REQUEST);
	}
}
