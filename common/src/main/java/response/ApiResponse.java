package response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
	private int code;
	private String message;
	private T data;

	public static <T> ApiResponse<T> of(int code, String message, T data) {
		return ApiResponse.<T>builder()
			.code(code)
			.message(message)
			.data(data)
			.build();
	}
}