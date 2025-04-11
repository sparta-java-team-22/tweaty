package com.tweaty.payment.global;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
	private int code;       // 200, 201 같은 HTTP 응답 코드
	private String message; // 성공 메시지
	private T data;         // 추가 데이터 (storeId 등)

	public static <T> ApiResponse<T> of(int code, String message, T data) {
		return ApiResponse.<T>builder()
			.code(code)
			.message(message)
			.data(data)
			.build();
	}
}