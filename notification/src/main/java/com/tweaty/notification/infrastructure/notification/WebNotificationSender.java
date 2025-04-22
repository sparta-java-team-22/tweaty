package com.tweaty.notification.infrastructure.notification;

import org.springframework.stereotype.Component;

import com.tweaty.notification.application.dto.NotificationDto;
import com.tweaty.notification.application.dto.NotificationRequestDto;
import com.tweaty.notification.application.dto.ReservationNotificationRequestDto;
import com.tweaty.notification.exception.InvalidNotificationTypeException;

import domain.NotiChannel;
import domain.NotiStatus;
import domain.NotiType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebNotificationSender {

	//회원가입
	public NotificationDto signUpSend(NotificationRequestDto requestDto) {

		NotificationDto notificationDto = new NotificationDto(requestDto);
		notificationDto.setNotiChannel(NotiChannel.WEB);
		notificationDto.setNotiStatus(NotiStatus.PENDING);

		String title = null;
		String message = null;

		switch (requestDto.getNotiType()) {

			case SIGNUP_USER:
				title = "[🎉 회원가입 완료]";
				message = "Tweaty 회원가입이 완료되었습니다.";
				break;

			case SIGNUP_OWNER:
				title = "사장님 회원가입 신청이 접수되었습니다.";
				message = "관리자의 승인 후 서비스 이용이 가능합니다. 승인까지는 최대 1~2일이 소요될 수 있습니다.";
				break;

			case SIGNUP_OWNER_APPROVED:
				title = "사장님 회원가입이 승인되었습니다.";
				message = "입점 신청이 승인되어 서비스를 이용하실 수 있습니다.";
				break;

			case SIGNUP_OWNER_REJECTED:
				title = "사장님 회원가입이 거절되었습니다.";
				message = "입점 신청이 거절되었습니다. 거절 사유를 확인해주세요.";
				break;

			default:
				throw new InvalidNotificationTypeException();

		}

		notificationDto.setNotiTitle(title);
		notificationDto.setNotiMessage(message);

		return notificationDto;

	}

	// 예약관련
	public NotificationDto reservationSend(ReservationNotificationRequestDto requestDto) {

		NotificationDto notificationDto = new NotificationDto(requestDto);
		notificationDto.setTargetId(requestDto.getTargetId());
		notificationDto.setNotiChannel(NotiChannel.WEB);
		notificationDto.setNotiStatus(NotiStatus.PENDING);

		String title = null;
		String message = null;

		switch (requestDto.getNotiType()) {

			case CREATED:
				title = "[🍽 예약 확정]";
				message = requestDto.getRestaurantName() + " 예약이 정상적으로 접수되었습니다.";
				break;

			case MODIFIED:
				title = "[🔄 예약 변경]";
				message = requestDto.getRestaurantName() + " 예약이 정상적으로 변경되었습니다.";
				break;

			case CANCELLED:
				title = "[❌ 예약 취소]";
				message = "예약이 정상적으로 취소되었습니다.";
				break;

			case REMINDER:
				title = "[⏰ 예약 알림]";
				message = "내일 예약이 있습니다! 잊지 마세요.";
				break;

			default:
				throw new InvalidNotificationTypeException();

		}

		notificationDto.setNotiTitle(title);
		notificationDto.setNotiMessage(message);

		return notificationDto;

	}
}
