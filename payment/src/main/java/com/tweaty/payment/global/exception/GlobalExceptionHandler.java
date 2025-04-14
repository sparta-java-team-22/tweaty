package com.tweaty.payment.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import exception.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
		return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
	}
}
