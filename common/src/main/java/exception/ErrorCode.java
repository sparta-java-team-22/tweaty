package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// 로그인 및 인증 관련 오류
	USER_NOT_FOUND("ERROR-U001", "사용자를 찾을 수 없습니다."),

	// 이벤트
	PROMOTION_DUPLICATED("ERROR-P001", "이미 존재하고 있는 이벤트입니다."),
	PROMOTION_NOT_FOUND("ERROR-P002", "찾을 수 없는 이벤트입니다.");

	private final String code;
	private final String message;
}
