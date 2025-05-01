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

	/**
 * Retrieves a paginated list of notifications for a specific receiver, filtered by notification channel and status.
 *
 * @param receiverId the unique identifier of the notification receiver
 * @param notiChannel the notification channel to filter by
 * @param notiStatus the notification status to filter by
 * @param pageable pagination information
 * @return a page of notifications matching the specified criteria
 */
Page<Notification> findByReceiverIdAndNotiChannelAndNotiStatus(UUID receiverId, NotiChannel notiChannel, NotiStatus notiStatus, Pageable pageable);

	/**
	 * Marks all unread web channel notifications as read for the specified receiver.
	 *
	 * @param id the receiver's unique identifier
	 * @return the number of notifications updated
	 */
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Notification n SET n.isRead = true WHERE n.receiverId = :id AND n.isRead = false AND n.notiChannel = 'WEB'")
	int markAllAsReadById(@Param("id") UUID id);

	/**
	 * Marks all undeleted web channel notifications for the specified receiver as deleted.
	 *
	 * Sets the {@code isDeleted} flag to {@code true} for all notifications belonging to the given receiver ID
	 * where {@code isDeleted} is {@code false} and {@code notiChannel} is {@code 'WEB'}.
	 *
	 * @param id the receiver's unique identifier
	 */
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Notification n SET n.isDeleted = true WHERE n.receiverId = :id AND n.isDeleted = false AND n.notiChannel = 'WEB'")
	void deleteAllNotification(@Param("id") UUID id);
}
