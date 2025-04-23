package com.tweaty.user.application.dto;

import java.util.Set;
import java.util.UUID;

import domain.NotiChannel;
import domain.NotiType;
import domain.TargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {

	private UUID receiverId;
	private TargetType targetType; //RESERVATION, WAITING, SIGNUP
	// private UUID targetId;
	private Set<NotiChannel> notiChannel; //WEB, EMAIL
	private NotiType notiType; //CREATED,UPDATED, CANCELLED, REMINDER, SIGNUP
	// private NotiStatus notiStatus; //PENDING, SENT, FAILED

	private String reason;

}
