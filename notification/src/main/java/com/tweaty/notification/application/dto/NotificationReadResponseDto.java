package com.tweaty.notification.application.dto;

import java.util.UUID;

import com.tweaty.notification.domain.model.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReadResponseDto {

	private UUID notiId;
	private boolean isRead;

	public NotificationReadResponseDto(Notification notification) {
		this.notiId = notification.getNotiId();
		this.isRead = notification.isRead();
	}
}
