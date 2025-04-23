package com.tweaty.notification.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.notification.domain.model.Notification;

import domain.NotiType;
import domain.TargetType;
import lombok.Getter;

@Getter
public class NotificationListDto {

	private UUID notiId;
	private TargetType targetType;
	private UUID targetId;
	private NotiType notiType;
	private String notiTitle;
	private String notiMessage;
	private boolean isRead;
	private LocalDateTime createdAt;

	public NotificationListDto(Notification notification) {

		this.notiId = notification.getNotiId();
		this.targetType = notification.getTargetType();
		this.targetId = notification.getTargetId();
		this.notiType = notification.getNotiType();
		this.notiTitle = notification.getNotiTitle();
		this.notiMessage = notification.getNotiMessage();
		this.isRead = notification.isRead();
		this.createdAt = notification.getCreatedAt();

	}

}
