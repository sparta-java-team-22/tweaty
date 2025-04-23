package response;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessResponse {

	public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
		return ResponseEntity.ok(
			ApiResponse.of(200, "요청이 성공했습니다.", data)
		);
	}

	public static ResponseEntity<ApiResponse<Map<String, Object>>> successMessageOnly(String message) {
		return ResponseEntity.ok(
			ApiResponse.of(200, message, new HashMap<>())
		);
	}

	public static <T> ResponseEntity<ApiResponse<T>> successWith(int code, String message, T data) {
		return ResponseEntity.status(HttpStatus.valueOf(code))
			.body(ApiResponse.of(code, message, data));
	}
}