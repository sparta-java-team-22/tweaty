package com.tweaty.notification.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.notification.application.dto.NotificationListDto;
import com.tweaty.notification.application.dto.NotificationListResponseDto;
import com.tweaty.notification.application.dto.NotificationReadAllResponseDto;
import com.tweaty.notification.application.dto.NotificationReadResponseDto;
import com.tweaty.notification.application.dto.NotificationRequestDto;
import com.tweaty.notification.application.dto.PageInfo;
import com.tweaty.notification.application.dto.ReservationNotificationRequestDto;
import com.tweaty.notification.application.service.NotificationService;
import com.tweaty.notification.presentation.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	//알림 생성 - 회원가입
	@PostMapping("/signup")
	public ResponseEntity<Void> createSignupNotification(
		@RequestBody NotificationRequestDto requestDto
	) {

		notificationService.createSignupNotification(requestDto);
		return ResponseEntity.ok().build();

	}

	//알림 생성 - 예약
	@PostMapping("/reservation")
	public ResponseEntity<Void> createReservationNotification(
		@RequestBody ReservationNotificationRequestDto requestDto
	) {

		notificationService.createReservationNotification(requestDto);
		return ResponseEntity.ok().build();

	}

	//알림 생성 - 리마인더
	@PostMapping("/reminder")
	public ResponseEntity<Void> createReminderNotification(
		@RequestBody ReservationNotificationRequestDto requestDto
	) {

		notificationService.createReservationNotification(requestDto);
		return ResponseEntity.ok().build();

	}

	//알림 조회
	@GetMapping
	public ResponseEntity<ApiResponse<NotificationListResponseDto>> getNotifications(
		@RequestHeader("X-USER-ID") UUID id,
		@RequestParam(value = "page", required = false, defaultValue = "1") int page
	) {

		Page<NotificationListDto> responseDtos = notificationService.getNotifications(id, page - 1);

		NotificationListResponseDto responseDto = NotificationListResponseDto.builder()
			.notifications(responseDtos.getContent())
			.pageInfo(PageInfo.builder()
				.page(responseDtos.getNumber())
				.size(responseDtos.getSize())
				.totalPages(responseDtos.getTotalPages())
				.totalElements(responseDtos.getTotalElements())
				.hasNext(responseDtos.hasNext())
				.hasPrevious(responseDtos.hasPrevious())
				.build())
			.build();

		ApiResponse<NotificationListResponseDto> response = ApiResponse.<NotificationListResponseDto>builder()
			.code(200)
			.message("알림 목록 조회 성공")
			.data(responseDto)
			.build();

		return ResponseEntity.ok(response);

	}

	//알림 읽음 처리
	@PatchMapping("/{notiId}/read")
	public ResponseEntity<ApiResponse<NotificationReadResponseDto>> markAsRead(
		@PathVariable UUID notiId,
		@RequestHeader("X-USER-ID") UUID id
	){

		NotificationReadResponseDto responseDto = notificationService.markAsRead(notiId, id);

		ApiResponse<NotificationReadResponseDto> response = ApiResponse.<NotificationReadResponseDto>builder()
			.code(200)
			.message("알림 읽음 처리 수정 성공")
			.data(responseDto)
			.build();

		return ResponseEntity.ok(response);
	}

	//알림 읽음 전체 처리
	@PatchMapping("/read-all")
	public ResponseEntity<ApiResponse<NotificationReadAllResponseDto>> markAsReadAll(
		@RequestHeader("X-USER-ID") UUID id
	){

		NotificationReadAllResponseDto responseDto = notificationService.markAsReadAll(id);

		ApiResponse<NotificationReadAllResponseDto> response = ApiResponse.<NotificationReadAllResponseDto>builder()
			.code(200)
			.message("알림 전체 읽음 처리 수정 성공")
			.data(responseDto)
			.build();

		return ResponseEntity.ok(response);
	}

	//알림 삭제
	@DeleteMapping("/{notiId}/delete")
	public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable UUID notiId, @RequestHeader("X-USER-ID") UUID id){

		notificationService.deleteNotification(notiId, id);

		ApiResponse<Void> response = ApiResponse.<Void>builder().code(200).message("알림 삭제 성공").build();

		return ResponseEntity.ok(response);
	}

	//알림 전체 삭제
	@DeleteMapping("/delete-all")
	public ResponseEntity<ApiResponse<Void>> deleteAllNotification(@RequestHeader("X-USER-ID") UUID id){

		notificationService.deleteAllNotification(id);

		ApiResponse<Void> response = ApiResponse.<Void>builder().code(200).message("알림 전체 삭제 성공").build();

		return ResponseEntity.ok(response);
	}

}
