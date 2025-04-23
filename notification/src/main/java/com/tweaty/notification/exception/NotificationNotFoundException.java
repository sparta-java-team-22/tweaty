package com.tweaty.notification.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class NotificationNotFoundException extends  CustomException {
	public NotificationNotFoundException() {
		super(ErrorCode.NOTIFICATION_NOT_FOUND, HttpStatus.NOT_FOUND);
	}
}
