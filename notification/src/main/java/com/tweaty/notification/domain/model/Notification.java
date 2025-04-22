package com.tweaty.notification.domain.model;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;

import com.tweaty.notification.application.dto.NotificationDto;

import base.BaseEntity;
import domain.NotiChannel;
import domain.NotiStatus;
import domain.NotiType;
import domain.TargetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_notification")
@Where(clause = "is_deleted = false")
public class Notification extends BaseEntity {

	@Id
	@UuidGenerator
	private UUID notiId;

	@Column(nullable = false)
	private UUID receiverId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TargetType targetType;

	private UUID targetId;

	@Enumerated(EnumType.STRING)
	@Column( nullable = false)
	private NotiChannel notiChannel;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotiType notiType;

	@Column(nullable = false)
	private String notiTitle;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String notiMessage;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotiStatus notiStatus;

	@Column(name = "is_read", nullable = false)
	private boolean isRead = false;

	public static Notification create(NotificationDto notificationDto) {

		Notification notification = new Notification();

		notification.receiverId = notificationDto.getReceiverId();
		notification.targetType = notificationDto.getTargetType();
		notification.targetId = notificationDto.getTargetId();
		notification.notiChannel = notificationDto.getNotiChannel();
		notification.notiType = notificationDto.getNotiType();
		notification.notiTitle = notificationDto.getNotiTitle();
		notification.notiMessage = notificationDto.getNotiMessage();
		notification.notiStatus = notificationDto.getNotiStatus();

		return notification;

	}

	public void markAsRead() {
		this.isRead = true;
	}
}
