package com.tweaty.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
	private final ErrorCode errorCode;
	private final HttpStatus httpStatus;

	public CustomException(ErrorCode errorCode, HttpStatus httpStatus) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
	}
}
