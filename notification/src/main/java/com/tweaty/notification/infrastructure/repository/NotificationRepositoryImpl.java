package com.tweaty.notification.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.tweaty.notification.application.dto.NotificationListDto;
import domain.NotiChannel;
import domain.NotiStatus;
import com.tweaty.notification.domain.model.Notification;
import com.tweaty.notification.domain.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

	private final JpaNotificationRepository jpaNotificationRepository;

	@Override
	public void save(Notification notification) {
		jpaNotificationRepository.save(notification);
	}

	@Override
	public Page<Notification> findByIdAndNotiChannelAndNotiStatus(UUID id, NotiChannel notiChannel,
		NotiStatus notiStatus, Pageable pageable) {

		return jpaNotificationRepository.findByReceiverIdAndNotiChannelAndNotiStatus(id, notiChannel, notiStatus, pageable);
	}

	@Override
	public Optional<Notification> findById(UUID notiId) {
		return jpaNotificationRepository.findById(notiId);
	}

	@Override
	public int markAllAsReadById(UUID id) {
		return jpaNotificationRepository.markAllAsReadById(id);
	}

	@Override
	public void deleteAllNotification(UUID id) {
		jpaNotificationRepository.deleteAllNotification(id);
	}

}
