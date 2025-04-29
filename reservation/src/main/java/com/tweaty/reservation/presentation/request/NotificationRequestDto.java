package com.tweaty.reservation.presentation.request;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import domain.NotiChannel;
import domain.NotiType;
import domain.TargetType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class NotificationRequestDto {
	private UUID receiverId;
	private TargetType targetType;
	private Set<NotiChannel> notiChannel;
	private NotiType notiType;
	private String reason;
	private UUID targetId;
	private String restaurantName;
	private int count;
	private LocalDateTime reservationDateTime;
}
