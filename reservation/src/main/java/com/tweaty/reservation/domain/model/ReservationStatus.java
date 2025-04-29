package com.tweaty.reservation.domain.model;

public enum ReservationStatus {
	READY("예약 결제"),
	CANCELLED("예약 취소"),
	FAILED("예약 실패"),
	COMPLETED("예약 완료"),
	NOME("찾을 수 없음");

	private final String description;

	ReservationStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public static ReservationStatus from(String reservationStatus) {
		for (ReservationStatus status : values()) {
			if (status.description.equalsIgnoreCase(reservationStatus.trim())) {
				return status;
			}
		}
		return NOME;
	}
}
