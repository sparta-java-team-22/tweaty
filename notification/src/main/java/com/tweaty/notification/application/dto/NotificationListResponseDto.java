package com.tweaty.notification.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotificationListResponseDto {

	private List<NotificationListDto> notifications;
	private PageInfo pageInfo;

}
