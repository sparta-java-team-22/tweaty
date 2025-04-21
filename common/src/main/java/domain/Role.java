package domain;

import exception.ErrorCode;

public enum Role {
	ROLE_CUSTOMER, //일반 유저
	ROLE_OWNER, //가게 주인
	ROLE_ADMIN; //관리자

	public static void checkAdmin(Role role) {
		if (!Role.ROLE_ADMIN.equals(role)) {
			throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED_ADMIN.getMessage());
		}
	}

	public static void checkOwner(Role role) {
		if (!Role.ROLE_OWNER.equals(role)) {
			throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED_OWNER.getMessage());
		}
	}
}
