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
	STORE_NOT_FOUND("ERROR-S001","식당을 찾을 수 없습니다."),
	STORE_ALREADY_DELETED("ERROR-S002","이미 삭제된 식당입니다."),

	// 메뉴
	MENU_NOT_FOUND("ERROR-M001","메뉴를 찾을 수 없습니다."),
	MENU_ALREADY_DELETED("ERROR-M002","이미 삭제된 메뉴입니다."),

	// 결제
	PAYMENT_NOT_FOUND("ERROR-P001","결제내역을 찾을 수 없습니다."),
	PAYMENT_FAIL("ERROR-P002","결제 처리 중 오류가 발생했습니다."),
	REFUND_NOT_FOUND("ERROR-P003","환불내역을 찾을 수 없습니다."),
	REFUND_ALREADY_USED("ERROR-P004","이미 환불된 결제입니다."),
	REFUND_FAIL("ERROR-P005","환불 처리 중 오류가 발생했습니다.");

	private final String code;
	private final String message;
}
