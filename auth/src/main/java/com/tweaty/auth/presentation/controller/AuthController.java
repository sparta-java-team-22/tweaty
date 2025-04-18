package com.tweaty.auth.presentation.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweaty.auth.application.dto.LoginRequestDto;
import com.tweaty.auth.application.dto.LoginResponseDto;
import com.tweaty.auth.application.dto.OwnerResponseDto;
import com.tweaty.auth.application.dto.OwnerSignUpRequestDto;
import com.tweaty.auth.application.dto.PasswordUpdateRequestDto;
import com.tweaty.auth.application.dto.UserResponseDto;
import com.tweaty.auth.application.dto.UserSignUpRequestDto;
import com.tweaty.auth.application.service.AuthService;
import com.tweaty.auth.presentation.common.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;
	private final ObjectMapper objectMapper;
	private final Validator validator;

	//일반 사용자 회원가입
	@PostMapping("/signup/user")
	public ResponseEntity<ApiResponse<UserResponseDto>> userSignUp(
		@RequestBody @Valid UserSignUpRequestDto requestDto) {

		UserResponseDto responseDto = authService.userSignUp(requestDto);

		ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
			.code(201)
			.message("회원가입 완료")
			.data(responseDto)
			.build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//가게 주인 사용자 회원가입
	@PostMapping("/signup/owner")
	public ResponseEntity<ApiResponse<OwnerResponseDto>> ownerSignUp(@RequestPart("request") String requestJson,
		@RequestPart("businessLicenseFile") MultipartFile file) {

		OwnerSignUpRequestDto requestDto;

		try {
			requestDto = objectMapper.readValue(requestJson, OwnerSignUpRequestDto.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("요청 JSON 파싱 실패", e);
		}

		Set<ConstraintViolation<OwnerSignUpRequestDto>> violations = validator.validate(requestDto);

		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<OwnerSignUpRequestDto> violation : violations) {
				sb.append(violation.getPropertyPath()).append(" ").append(violation.getMessage());
			}
			throw new IllegalArgumentException("유효성 검사 실패 : " + sb.toString());
		}

		OwnerResponseDto responseDto = authService.ownerSignUp(requestDto, file);

		ApiResponse<OwnerResponseDto> response = ApiResponse.<OwnerResponseDto>builder()
			.code(201)
			.message("회원가입 완료. 관리자 승인까지 대기")
			.data(responseDto)
			.build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//로그인
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto requestDto) {

		LoginResponseDto responseDto = authService.login(requestDto);

		ApiResponse<LoginResponseDto> response = ApiResponse.<LoginResponseDto>builder()
			.code(200)
			.message("로그인 성공")
			.data(responseDto)
			.build();

		return ResponseEntity.ok(response);
	}

	//비밀번호 수정
	@PutMapping("/password")
	public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestHeader("X-USERNAME") String username,
		@RequestBody PasswordUpdateRequestDto requestDto) {

		authService.updatePassword(username, requestDto);

		ApiResponse<Void> response = ApiResponse.<Void>builder().code(200).message("비밀번호 수정 성공").build();

		return ResponseEntity.ok(response);
	}

}
