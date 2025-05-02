package com.tweaty.notification.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.notification.application.dto.NotificationDto;
import com.tweaty.notification.application.dto.NotificationListDto;
import com.tweaty.notification.application.dto.NotificationReadAllResponseDto;
import com.tweaty.notification.application.dto.NotificationReadResponseDto;
import com.tweaty.notification.application.dto.NotificationRequestDto;
import com.tweaty.notification.application.dto.ReservationNotificationRequestDto;
import com.tweaty.notification.application.dto.UserMailDto;
import domain.NotiChannel;
import domain.NotiStatus;
import com.tweaty.notification.domain.model.Notification;
import com.tweaty.notification.domain.repository.NotificationRepository;
import com.tweaty.notification.exception.InvalidNotificationChannelException;
import com.tweaty.notification.exception.NotificationForbiddenAccess;
import com.tweaty.notification.exception.NotificationNotFoundException;
import com.tweaty.notification.infrastructure.client.UserServiceClient;
import com.tweaty.notification.infrastructure.notification.EmailNotificationSender;
import com.tweaty.notification.infrastructure.notification.WebNotificationSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
	
	private final NotificationRepository notificationRepository;
	private final WebNotificationSender webNotificationSender;
	private final EmailNotificationSender emailNotificationSender;
	private final UserServiceClient userServiceClient;

	public void createSignupNotification(NotificationRequestDto requestDto) {

		for(NotiChannel notiChannel : requestDto.getNotiChannel()) {

			NotificationDto notificationDto = new NotificationDto();
			Notification notification;

			switch (notiChannel) {
				case WEB:
					//웹 알림
					notificationDto = webNotificationSender.signUpSend(requestDto);
					break;

				case EMAIL:
					//이메일 알림
					UserMailDto receiverEmail = userServiceClient.getUserMail(requestDto.getReceiverId(), "true");
					notificationDto = emailNotificationSender.signUpSend(requestDto, receiverEmail);

					break;

				default:
					throw new InvalidNotificationChannelException();
			}

			notificationDto.setNotiStatus(NotiStatus.SENT);
			notification = Notification.create(notificationDto);

			notificationRepository.save(notification);
		}
	}

	public void createReservationNotification(ReservationNotificationRequestDto requestDto) {

		for(NotiChannel notiChannel : requestDto.getNotiChannel()) {

			NotificationDto notificationDto = new NotificationDto();
			Notification notification;

			switch (notiChannel) {
				case WEB:
					//웹 알림
					notificationDto = webNotificationSender.reservationSend(requestDto);
					break;

				case EMAIL:
					//이메일 알림
					UserMailDto receiverEmail = userServiceClient.getUserMail(requestDto.getReceiverId(), "true");
					notificationDto = emailNotificationSender.reservationSend(requestDto, receiverEmail);
					break;

				default:
					throw new InvalidNotificationChannelException();
			}

			notificationDto.setNotiStatus(NotiStatus.SENT);
			notification = Notification.create(notificationDto);

			notificationRepository.save(notification);
		}
	}

	@Transactional(readOnly = true)
	public Page<NotificationListDto> getNotifications(UUID id, int page) {

		int limit = 30;

		Pageable pageable = PageRequest.of(page, limit);

		Page<Notification> notifications = notificationRepository.findByIdAndNotiChannelAndNotiStatus(id, NotiChannel.WEB, NotiStatus.SENT, pageable);

		Page<NotificationListDto> notificationListDtos = notifications.map(NotificationListDto::new);

		return notificationListDtos;

	}

	@Transactional
	public NotificationReadResponseDto markAsRead(UUID notiId, UUID id) {

		Notification notification = notificationRepository.findById(notiId)
			.orElseThrow(NotificationNotFoundException::new);

		if (!notification.getReceiverId().equals(id)) {
			throw new NotificationForbiddenAccess();
		}

		notification.markAsRead();

		return new NotificationReadResponseDto(notification);
	}

	@Transactional
	public NotificationReadAllResponseDto markAsReadAll(UUID id) {

		int updatedCount = notificationRepository.markAllAsReadById(id);

		return new NotificationReadAllResponseDto(updatedCount);

	}

	@Transactional
	public void deleteNotification(UUID notiId, UUID id) {

		Notification notification = notificationRepository.findById(notiId)
			.orElseThrow(NotificationNotFoundException::new);

		if (!notification.getReceiverId().equals(id)) {
			throw new NotificationForbiddenAccess();
		}

		notification.softDelete();

	}

	@Transactional
	public void deleteAllNotification(UUID id) {

		notificationRepository.deleteAllNotification(id);

	}
}
