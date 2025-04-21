package com.tweaty.notification.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public class NotificationForbiddenAccess extends CustomException{
	public NotificationForbiddenAccess() {
		super(ErrorCode.NOTIFICATION_FORBIDDEN_ACCESS, HttpStatus.FORBIDDEN);
	}
}
