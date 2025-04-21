package com.tweaty.notification.infrastructure.notification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.tweaty.notification.application.dto.NotificationDto;
import com.tweaty.notification.application.dto.NotificationRequestDto;
import com.tweaty.notification.application.dto.ReservationNotificationRequestDto;
import com.tweaty.notification.application.dto.UserMailDto;
import com.tweaty.notification.exception.InvalidNotificationTypeException;

import domain.NotiChannel;
import domain.NotiStatus;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationSender {

	private final JavaMailSender javaMailSender;

	//회원가입
	public NotificationDto signUpSend(NotificationRequestDto requestDto, UserMailDto receiverEmail) {

		NotificationDto notificationDto = new NotificationDto(requestDto);
		notificationDto.setNotiChannel(NotiChannel.EMAIL);
		notificationDto.setNotiStatus(NotiStatus.PENDING);

		String title = null;
		String message = null;

		switch(requestDto.getNotiType()){

			case SIGNUP_USER:
				title = "[Tweaty] 회원가입이 완료되었습니다.";
				message = "Tweaty 회원가입을 해주셔서 감사합니다. 바로 모든 서비스를 이용해보세요!";
				break;

			case SIGNUP_OWNER:
				title = "[Tweaty] 사장님 회원가입 신청이 접수되었습니다.";
				message = "관리자의 승인 후 서비스 이용이 가능합니다. 승인까지는 최대 1~2일이 소요될 수 있습니다.";
				break;

			case SIGNUP_OWNER_APPROVED:
				title = "[Tweaty] 사장님 회원가입이 승인되었습니다.";
				message = "입점 신청이 승인되어 서비스를 이용하실 수 있습니다.";
				break;

			case SIGNUP_OWNER_REJECTED:
				title = "[Tweaty] 사장님 회원가입이 거절되었습니다.";
				message = "입점 신청이 거절되었습니다. 거절 사유를 확인해주세요.";
				break;

			default:
				throw new InvalidNotificationTypeException();

		}

		notificationDto.setNotiTitle(title);
		notificationDto.setNotiMessage(message);

		try {
			sendMail(receiverEmail.getEmail(), title, message);
			notificationDto.setNotiStatus(NotiStatus.SENT);
		} catch(Exception e){
			log.error("이메일 발송 실패", e);
			notificationDto.setNotiStatus(NotiStatus.FAILED);
			//재시도 전략
		}

		return notificationDto;
	}

	//회원가입
	public NotificationDto reservationSend(ReservationNotificationRequestDto requestDto, UserMailDto receiverEmail) {

		NotificationDto notificationDto = new NotificationDto(requestDto);
		notificationDto.setTargetId(requestDto.getTargetId());
		notificationDto.setNotiChannel(NotiChannel.EMAIL);
		notificationDto.setNotiStatus(NotiStatus.PENDING);

		String title = null;
		String message = null;
		String formatReservationTime = formatTime(requestDto.getReservationDateTime());

		switch(requestDto.getNotiType()){

			case CREATED:
				title = "[Tweaty] 예약이 완료되었습니다";
				message = formatReservationTime + " " + requestDto.getRestaurantName() + " (" + requestDto.getCount() + ") 예약이 완료되었습니다.";
				break;

			case MODIFIED:
				title = "[Tweaty] 예약 정보가 변경되었습니다.";
				message = formatReservationTime + " " + requestDto.getRestaurantName() + " (" + requestDto.getCount() + ") 으로 예약이 변경되었습니다.";
				break;

			case CANCELLED:
				title = "[Tweaty] 예약이 취소되었습니다.";
				message = formatReservationTime + " " + requestDto.getRestaurantName() + " (" + requestDto.getCount() + ") 예약이 취소되었습니다.";
				break;

			case REMINDER:
				title = "[Tweaty] 내일 예약이 있습니다.";
				message = formatReservationTime + " " + requestDto.getRestaurantName() + " 에 예약(" + requestDto.getCount() + ") 이 있습니다.";
				break;

			default:
				throw new InvalidNotificationTypeException();

		}

		notificationDto.setNotiTitle(title);
		notificationDto.setNotiMessage(message);


		try {
			sendMail(receiverEmail.getEmail(), title, message);
			notificationDto.setNotiStatus(NotiStatus.SENT);
		} catch(Exception e){
			log.error("이메일 발송 실패", e);
			notificationDto.setNotiStatus(NotiStatus.FAILED);
			//재시도 전략
		}

		return notificationDto;
	}

	public void sendMail(String receiverEmail, String title, String message) throws MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

		mimeMessageHelper.setTo(receiverEmail);
		mimeMessageHelper.setSubject(title);
		mimeMessageHelper.setText(message, true);

		javaMailSender.send(mimeMessage);

	}

	private String formatTime(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h:mm", Locale.KOREAN);
		return localDateTime.format(formatter);
	}
}
