package com.tweaty.auth.presentation.controller;

import java.util.Map;
import java.util.Set;

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
import com.tweaty.auth.application.dto.TokenReissueRequestDto;
import com.tweaty.auth.application.dto.UserResponseDto;
import com.tweaty.auth.application.dto.UserSignUpRequestDto;
import com.tweaty.auth.application.service.AuthService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import response.ApiResponse;
import response.SuccessResponse;

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

		return SuccessResponse.successWith(201, "회원가입 완료", responseDto);
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

		return SuccessResponse.successWith(201, "회원가입 완료. 관리자 승인까지 대기", responseDto);
	}

	//로그인
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto requestDto) {

		LoginResponseDto responseDto = authService.login(requestDto);

		return SuccessResponse.successWith(200, "로그인 성공", responseDto);
	}

	/**
	 * Updates the password for the specified user.
	 *
	 * @param username the username extracted from the "X-USERNAME" request header
	 * @param requestDto the password update request payload
	 * @return a response indicating successful password update
	 */
	@PutMapping("/password")
	public ResponseEntity<ApiResponse<Map<String, Object>>> updatePassword(@RequestHeader("X-USERNAME") String username,
		@RequestBody PasswordUpdateRequestDto requestDto) {

		authService.updatePassword(username, requestDto);

		return SuccessResponse.successMessageOnly("비밀번호 수정 성공");
	}

	/**
	 * Reissues authentication tokens using the provided authorization header and refresh token.
	 *
	 * @param authHeader the value of the Authorization header from the request
	 * @param requestDto the request body containing the refresh token
	 * @return a response entity containing the new authentication tokens and a success message
	 */
	@PostMapping("/reissue")
	public ResponseEntity<ApiResponse<LoginResponseDto>> reissueToken(
		@RequestHeader("Authorization") String authHeader,
		@RequestBody TokenReissueRequestDto requestDto
	) {

		String refreshToken = requestDto.getRefreshToken();
		LoginResponseDto responseDto = authService.reissueToken(authHeader, refreshToken);

		return SuccessResponse.successWith(200, "토큰 재발급 성공", responseDto);

	}

	/**
	 * Logs out the authenticated user by invalidating their session or token.
	 *
	 * @param authHeader the Authorization header containing the user's access token
	 * @return a response indicating successful logout
	 */
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Map<String, Object>>> logout(
		@RequestHeader("Authorization") String authHeader
	) {

		authService.logout(authHeader);

		return SuccessResponse.successMessageOnly("로그아웃 성공");

	}

}
