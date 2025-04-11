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
	PROMOTION_NOT_FOUND("ERROR-P002", "찾을 수 없는 이벤트입니다."),

	// 식당
	STORE_NOT_FOUND("ERROR-S001", "식당을 찾을 수 없습니다."),
	STORE_ALREADY_DELETED("ERROR-S002", "삭제된 식당입니다."),

	// 메뉴
	MENU_NOT_FOUND("ERROR-M001", "메뉴를 찾을 수 없습니다."),
	MENU_ALREADY_DELETED("ERROR-M002", "삭제된 메뉴입니다."),

	// 쿠폰
	COUPON_NOT_FOUND("ERROR-C001", "쿠폰을 찾을 수 없습니다."),
	COUPON_EXPIRED("ERROR-C002", "쿠폰 발급 기간이 아닙니다."),
	COUPON_OUT_OF_STOCK("ERROR-C003", "남은 쿠폰 수량이 없습니다."),
	COUPON_ALREADY_ISSUED("ERROR-C004", "고객에게 이미 발급된 쿠폰입니다.");

	private final String code;
	private final String message;
}
