package com.tweaty.notification.application.dto;

import java.util.UUID;

import domain.NotiChannel;
import domain.NotiStatus;
import domain.NotiType;
import domain.TargetType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDto {

	private UUID receiverId;
	private TargetType targetType; //RESERVATION, WAITING, SIGNUP
	private UUID targetId;
	private NotiChannel notiChannel;
	private NotiType notiType; //CREATED,UPDATED, CANCELLED, REMINDER, SIGNUP
	private String notiTitle;
	private String notiMessage;
	private NotiStatus notiStatus; //PENDING, SENT, FAILED

	public NotificationDto(NotificationRequestDto requestDto) {
		this.receiverId = requestDto.getReceiverId();
		this.targetType = requestDto.getTargetType();
		this.notiType = requestDto.getNotiType();
	}
}
