package com.tweaty.notification.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tweaty.notification.application.dto.NotificationListDto;
import domain.NotiChannel;
import domain.NotiStatus;
import com.tweaty.notification.domain.model.Notification;

public interface JpaNotificationRepository extends JpaRepository<Notification, UUID> {

	Page<Notification> findByReceiverIdAndNotiChannelAndNotiStatus(UUID receiverId, NotiChannel notiChannel, NotiStatus notiStatus, Pageable pageable);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Notification n SET n.isRead = true WHERE n.receiverId = :id AND n.isRead = false AND n.notiChannel = 'WEB'")
	int markAllAsReadById(@Param("id") UUID id);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Notification n SET n.isDeleted = true WHERE n.receiverId = :id AND n.isDeleted = false AND n.notiChannel = 'WEB'")
	void deleteAllNotification(@Param("id") UUID id);
}
