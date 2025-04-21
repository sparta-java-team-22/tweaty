package com.tweaty.notification.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tweaty.notification.application.dto.NotificationListDto;
import domain.NotiChannel;
import domain.NotiStatus;
import com.tweaty.notification.domain.model.Notification;

public interface NotificationRepository {

	void save(Notification notification);

	Page<Notification> findByIdAndNotiChannelAndNotiStatus(UUID id, NotiChannel notiChannel, NotiStatus notiStatus, Pageable pageable);

	Optional<Notification> findById(UUID notiId);

	int markAllAsReadById(UUID id);

	void deleteAllNotification(UUID id);
}
