package com.tweaty.user.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.user.application.dto.OwnerStatusChangeResponseDto;
import com.tweaty.user.application.dto.PageInfo;
import com.tweaty.user.application.dto.UserListDto;
import com.tweaty.user.application.dto.UserListReponseDto;
import com.tweaty.user.application.dto.UserViewResponseDto;
import com.tweaty.user.application.service.UserService;

import domain.Role;
import lombok.RequiredArgsConstructor;
import response.ApiResponse;
import response.SuccessResponse;

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class AdminController {

	private final UserService userService;

	//회원 단일 조회
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<UserViewResponseDto>> getUser(@PathVariable UUID id) {

		UserViewResponseDto responseDto = userService.getUser(id);

		return SuccessResponse.successWith(200, "회원 조회 성공", responseDto);

	}

	//회원 목록 조회 및 검색
	@GetMapping
	public ResponseEntity<ApiResponse<UserListReponseDto>> getUserList(
		@RequestParam(value = "key", required = false, defaultValue = "") String key,
		@RequestParam(value = "role", required = false, defaultValue = "ROLE_CUSTOMER") Role role,
		@RequestParam(value = "page", required = false, defaultValue = "1") int page,
		@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
		@RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
		@RequestParam(value = "order", required = false, defaultValue = "asc") String order) {

		Page<UserListDto> responseDtos = userService.getUserList(key, role, page - 1, limit, sortBy, order);

		UserListReponseDto responseDto = UserListReponseDto.builder()
			.users(responseDtos.getContent())
			.pageInfo(PageInfo.builder()
				.page(responseDtos.getNumber())
				.size(responseDtos.getSize())
				.totalPages(responseDtos.getTotalPages())
				.totalElements(responseDtos.getTotalElements())
				.hasNext(responseDtos.hasNext())
				.hasPrevious(responseDtos.hasPrevious())
				.build())
			.build();

		return SuccessResponse.successWith(200, "회원 목록 조회 성공", responseDto);

	}

	//승인 대기 가게 주인 사용자 목록
	@GetMapping("/pending-owners")
	public ResponseEntity<ApiResponse<UserListReponseDto>> getPendingOwnersList(
		@RequestParam(value = "page", required = false, defaultValue = "1") int page,
		@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
		@RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
		@RequestParam(value = "order", required = false, defaultValue = "asc") String order) {

		Page<UserListDto> responseDtos = userService.getPendingOwnersList(page - 1, limit, sortBy, order);

		UserListReponseDto responseDto = UserListReponseDto.builder()
			.users(responseDtos.getContent())
			.pageInfo(PageInfo.builder()
				.page(responseDtos.getNumber())
				.size(responseDtos.getSize())
				.totalPages(responseDtos.getTotalPages())
				.totalElements(responseDtos.getTotalElements())
				.hasNext(responseDtos.hasNext())
				.hasPrevious(responseDtos.hasPrevious())
				.build())
			.build();

		return SuccessResponse.successWith(200, "승인 대기 가게 주인 목록 조회 성공", responseDto);

	}

	//승인 거절 가게 주인 사용자 목록
	@GetMapping("/rejected-owners")
	public ResponseEntity<ApiResponse<UserListReponseDto>> getRejectedOwnersList(
		@RequestParam(value = "page", required = false, defaultValue = "1") int page,
		@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
		@RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
		@RequestParam(value = "order", required = false, defaultValue = "asc") String order) {

		Page<UserListDto> responseDtos = userService.getRejectedOwnersList(page - 1, limit, sortBy, order);

		UserListReponseDto responseDto = UserListReponseDto.builder()
			.users(responseDtos.getContent())
			.pageInfo(PageInfo.builder()
				.page(responseDtos.getNumber())
				.size(responseDtos.getSize())
				.totalPages(responseDtos.getTotalPages())
				.totalElements(responseDtos.getTotalElements())
				.hasNext(responseDtos.hasNext())
				.hasPrevious(responseDtos.hasPrevious())
				.build())
			.build();

		return SuccessResponse.successWith(200, "승인 거절 가게 주인 목록 조회 성공", responseDto);

	}

	//가게 주인 회원 승인
	@PatchMapping("/{id}/approve")
	public ResponseEntity<ApiResponse<OwnerStatusChangeResponseDto>> approveOwner(@PathVariable UUID id) {

		OwnerStatusChangeResponseDto responseDto = userService.approveOwner(id);

		return SuccessResponse.successWith(200, "회원 승인 성공", responseDto);

	}

	//가게 주인 회원 거절
	@PatchMapping("/{id}/reject")
	public ResponseEntity<ApiResponse<OwnerStatusChangeResponseDto>> rejectOwner(
		@PathVariable UUID id,
		@RequestParam(required = false) String reason
	) {

		OwnerStatusChangeResponseDto responseDto = userService.rejectOwner(id, reason);

		return SuccessResponse.successWith(200, "회원 거절 성공", responseDto);

	}

}