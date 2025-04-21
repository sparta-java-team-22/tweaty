package com.tweaty.user.presentation.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.user.application.dto.OwnerCreateRequestDto;
import com.tweaty.user.application.dto.OwnerResponseDto;
import com.tweaty.user.application.dto.UserCreateRequestDto;
import com.tweaty.user.application.dto.UserDto;
import com.tweaty.user.application.dto.UserInfoResponseDto;
import com.tweaty.user.application.dto.UserMailDto;
import com.tweaty.user.application.dto.UserResponseDto;
import com.tweaty.user.application.dto.UserUpdateRequestDto;
import com.tweaty.user.application.dto.UserUpdateResponseDto;
import com.tweaty.user.application.service.UserService;
import com.tweaty.user.exception.AccessDeniedException;
import com.tweaty.user.presentation.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	//일반 사용자 회원가입
	@PostMapping("/internal/signup/user")
	public UserResponseDto userSignUp(@RequestBody UserCreateRequestDto requestDto,
		@RequestHeader("internal-request") String internalRequest) {

		if ("true".equals(internalRequest)) {
			return userService.userSignUp(requestDto);
		}

		throw new AccessDeniedException();

	}

	//가게 주인 사용자 회원가입
	@PostMapping("/internal/signup/owner")
	public OwnerResponseDto ownerSignUp(@RequestBody OwnerCreateRequestDto requestDto,
		@RequestHeader("internal-request") String internalRequest) {

		if ("true".equals(internalRequest)) {
			return userService.ownerSignUp(requestDto);
		}

		throw new AccessDeniedException();

	}

	@GetMapping("/internal")
	public UserDto getUserByUsername(@RequestParam("username") String username,
		@RequestHeader("internal-request") String internalRequest) {

		if ("true".equals(internalRequest)) {
			return userService.getUserByUsername(username);
		}

		throw new AccessDeniedException();

	}

	//내 정보 보기
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserInfoResponseDto>> getMyInfo(@RequestHeader("X-USER-ID") UUID id,
		@RequestHeader("X-USER-ROLE") String role) {

		UserInfoResponseDto responseDto;

		if ("ROLE_OWNER".equals(role)) {
			responseDto = userService.getOwnerInfo(id);
		} else if ("ROLE_CUSTOMER".equals(role) || "ROLE_ADMIN".equals(role)) {
			responseDto = userService.getCustomerInfo(id);
		} else {
			throw new AccessDeniedException();
		}

		ApiResponse<UserInfoResponseDto> response = ApiResponse.<UserInfoResponseDto>builder()
			.code(200)
			.message("내 정보 조회 성공")
			.data(responseDto)
			.build();

		return ResponseEntity.ok(response);

	}

	//회원 정보 수정
	@PatchMapping
	public ResponseEntity<ApiResponse<UserUpdateResponseDto>> updateUserInfo(@RequestHeader("X-USER-ID") String id,
		@RequestBody UserUpdateRequestDto requestDto) {

		UserUpdateResponseDto responseDto = userService.updateUserInfo(UUID.fromString(id), requestDto);

		ApiResponse<UserUpdateResponseDto> response = ApiResponse.<UserUpdateResponseDto>builder()
			.code(200)
			.message("회원 정보 수정 성공")
			.data(responseDto)
			.build();

		return ResponseEntity.ok(response);

	}

	//비밀번호 수정
	@PutMapping("/internal/{id}/password")
	public ResponseEntity<Void> updatePassword(@PathVariable UUID id, @RequestBody String encodedPassword,
		@RequestHeader("internal-request") String internalRequest) {

		if ("true".equals(internalRequest)) {
			userService.updatePassword(id, encodedPassword);
			return ResponseEntity.ok().build();
		}

		throw new AccessDeniedException();

	}

	//회원 탈퇴
	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteUser(@RequestHeader("X-USER-ID") String id) {

		userService.deleteUser(UUID.fromString(id));

		ApiResponse<Void> response = ApiResponse.<Void>builder().code(200).message("회원 탈퇴 성공").build();

		return ResponseEntity.ok(response);

	}

	@GetMapping("/internal/mail")
	public UserMailDto getUserMail(@RequestParam("receiverId") UUID receiverId,
		@RequestHeader("internal-request") String internalRequest) {

		if ("true".equals(internalRequest)) {
			return userService.getUserMail(receiverId);
		}

		throw new AccessDeniedException();

	}
}

