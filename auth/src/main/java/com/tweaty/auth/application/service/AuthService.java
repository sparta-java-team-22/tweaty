package com.tweaty.auth.application.service;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tweaty.auth.application.dto.LoginRequestDto;
import com.tweaty.auth.application.dto.LoginResponseDto;
import com.tweaty.auth.application.dto.OwnerCreateRequestDto;
import com.tweaty.auth.application.dto.OwnerResponseDto;
import com.tweaty.auth.application.dto.OwnerSignUpRequestDto;
import com.tweaty.auth.application.dto.PasswordUpdateRequestDto;
import com.tweaty.auth.application.dto.UserCreateRequestDto;
import com.tweaty.auth.application.dto.UserDto;
import com.tweaty.auth.application.dto.UserResponseDto;
import com.tweaty.auth.application.dto.UserSignUpRequestDto;
import com.tweaty.auth.infrastructure.client.S3Uploader;
import com.tweaty.auth.infrastructure.client.UserServiceClient;
import com.tweaty.auth.infrastructure.security.AuthTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserServiceClient userServiceClient;
	private final S3Uploader s3Uploader;
	private final AuthTokenProvider tokenProvider;

	public UserResponseDto userSignUp(UserSignUpRequestDto requestDto) {

		String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());

		UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(requestDto.getUsername(),
			encryptedPassword, requestDto.getName(), requestDto.getPhoneNumber(), requestDto.getEmail(),
			requestDto.getRole());

		return userServiceClient.userSignUp(userCreateRequestDto, "true");

	}

	public OwnerResponseDto ownerSignUp(OwnerSignUpRequestDto requestDto, MultipartFile file) {

		// 사업자 등록증 업로드
		String uploadedUrl = s3Uploader.upload(file, "business_license");

		String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());

		OwnerCreateRequestDto ownerCreateRequestDto = new OwnerCreateRequestDto(requestDto.getUsername(),
			encryptedPassword, requestDto.getName(), requestDto.getPhoneNumber(), requestDto.getEmail(),
			requestDto.getRole(), requestDto.getBusinessNumber(), uploadedUrl);

		return userServiceClient.ownerSignUp(ownerCreateRequestDto, "true");

	}

	public LoginResponseDto login(LoginRequestDto requestDto) {

		UserDto user = userServiceClient.getUserByUsername(requestDto.getUsername(), "true");

		if (!BCrypt.checkpw(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		UserResponseDto responseDto = new UserResponseDto(user);

		String accessToken = tokenProvider.createAccessToken(user);
		String refreshToken = UUID.randomUUID().toString();

		return new LoginResponseDto(accessToken, refreshToken, "Bearer", 3600L, responseDto);

	}

	public void updatePassword(String username, PasswordUpdateRequestDto requestDto) {

		UserDto user = userServiceClient.getUserByUsername(username, "true");

		if (!BCrypt.checkpw(requestDto.getCurrentPassword(), user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());
		userServiceClient.updatePassword(user.getId(), encodedNewPassword, "true");

	}

}
