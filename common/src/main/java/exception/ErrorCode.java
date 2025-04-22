package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// 로그인 및 인증 관련 오류
	USER_NOT_FOUND("ERROR-U001", "사용자를 찾을 수 없습니다."),
	UNAUTHORIZED_ADMIN("ERROR-U002", "관리자 권한이 아닙니다."),
	UNAUTHORIZED_OWNER("ERROR-U002", "가게 주인 권한이 아닙니다."),
	USER_FORBIDDEN("ERROR-U003", "사용자 권한이 없습니다."),
	USERNAME_ALREADY_EXISTS("ERROR-U004", "이미 사용 중인 아이디입니다."),
	EMAIL_ALREADY_EXISTS("ERROR-U005", "이미 사용 중인 이메일입니다."),
	PHONE_ALREADY_EXISTS("ERROR-U006", "이미 사용 중인 전화번호입니다."),
	ALREADY_APPROVED("ERROR-U007", "이미 승인된 사용자입니다."),
	INVALID_APPROVAL_STATUS("ERROR-U008", "유효하지 않은 승인 상태입니다."),
	INVALID_ROLE("ERROR-U009", "유효하지 않는 권한입니다."),
	INVALID_PASSWORD("ERROR-U010", "비밀번호가 올바르지 않습니다."),

	//알림
	INVALID_NOTIFICATION_TYPE("ERROR-N001", "알 수 없는 알림 타입입니다."),
	INVALID_NOTIFICATION_CHANNEL("ERROR-N002", "알 수 없는 알림 채널입니다."),
	NOTIFICATION_NOT_FOUND("ERROR-N003", "알림이 존재하지 않습니다."),
	NOTIFICATION_FORBIDDEN_ACCESS("ERROR-N004", "본인의 알림만 처리할 수 있습니다."),

	// 이벤트
	PROMOTION_DUPLICATED("ERROR-P001", "이미 존재하고 있는 이벤트입니다."),
	PROMOTION_NOT_FOUND("ERROR-P002", "찾을 수 없는 이벤트입니다."),
	PROMOTION_COUPON_NOT_FOUND("ERROR-P003", "이벤트에 해당하는 쿠폰이 없습니다."),
	PROMOTION_ISSUE_COUPON_API_FAIL("ERROR-P004", "쿠폰 서비스의 쿠폰 발급 api 호출이 실패되었습니다."),
	PROMOTION_NOT_ACTIVE("ERROR-P005", "이벤트 기간이 아닙니다."),

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
	REFUND_FAIL("ERROR-P005","환불 처리 중 오류가 발생했습니다."),

	// 쿠폰
	COUPON_NOT_FOUND("ERROR-C001", "쿠폰을 찾을 수 없습니다."),
	COUPON_EXPIRED("ERROR-C002", "쿠폰 발급 기간이 아닙니다."),
	COUPON_OUT_OF_STOCK("ERROR-C003", "남은 쿠폰 수량이 없습니다."),
	COUPON_ALREADY_ISSUED("ERROR-C004", "고객에게 이미 발급된 쿠폰입니다."),
	COUPON_ISSUE_NOT_FOUND("ERROR-C005", "고객에게 발급된 쿠폰을 찾을 수 없습니다.");

	private final String code;
	private final String message;
}
