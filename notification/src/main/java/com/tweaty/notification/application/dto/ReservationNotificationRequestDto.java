package com.tweaty.notification.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationNotificationRequestDto extends NotificationRequestDto {

	private UUID targetId; //예약, 대기 id
	private String restaurantName;
	private int count;
	private LocalDateTime reservationDateTime;

}
