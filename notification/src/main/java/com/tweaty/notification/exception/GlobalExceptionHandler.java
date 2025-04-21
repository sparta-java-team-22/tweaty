package com.tweaty.notification.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import exception.ErrorResponse;
import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		return ResponseEntity
			.status(e.getHttpStatus())
			.body(new ErrorResponse(e.getErrorCode()));
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<Map<String, String>> handleFeignException(FeignException e) {

		String responseBody = e.contentUTF8();

		String code = "ERROR-FEIGN";
		String message = "Feign 요청 중 오류가 발생했습니다.";
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;;

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(responseBody);

			if (root.isArray() && !root.isEmpty()) {
				root = root.get(0);
			}

			code = root.path("code").asText(code);
			message = root.path("message").asText(message);
			status = HttpStatus.valueOf(e.status());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Map<String, String> error = new HashMap<>();
		error.put("code", code);
		error.put("message", message);

		return ResponseEntity.status(status).body(error);

	}
}
