package com.tweaty.auth.application.service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tweaty.auth.application.dto.LoginRequestDto;
import com.tweaty.auth.application.dto.LoginResponseDto;
import com.tweaty.auth.application.dto.NotificationRequestDto;
import com.tweaty.auth.application.dto.OwnerCreateRequestDto;
import com.tweaty.auth.application.dto.OwnerResponseDto;
import com.tweaty.auth.application.dto.OwnerSignUpRequestDto;
import com.tweaty.auth.application.dto.PasswordUpdateRequestDto;
import com.tweaty.auth.application.dto.UserCreateRequestDto;
import com.tweaty.auth.application.dto.UserDto;
import com.tweaty.auth.application.dto.UserResponseDto;
import com.tweaty.auth.application.dto.UserSignUpRequestDto;
import com.tweaty.auth.exception.InvalidPasswordException;
import com.tweaty.auth.infrastructure.client.NotificationServiceClient;
import com.tweaty.auth.infrastructure.client.S3Uploader;
import com.tweaty.auth.infrastructure.client.UserServiceClient;
import com.tweaty.auth.infrastructure.security.AuthTokenProvider;

import domain.NotiChannel;
import domain.NotiType;
import domain.TargetType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserServiceClient userServiceClient;
	private final NotificationServiceClient notificationServiceClient;
	private final S3Uploader s3Uploader;
	private final AuthTokenProvider tokenProvider;
	private final StringRedisTemplate redisTemplate;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public UserResponseDto userSignUp(UserSignUpRequestDto requestDto) {

		String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());

		UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(requestDto.getUsername(),
			encryptedPassword, requestDto.getName(), requestDto.getPhoneNumber(), requestDto.getEmail(),
			requestDto.getRole());

		UserResponseDto responseDto =  userServiceClient.userSignUp(userCreateRequestDto, "true");

		UserDto user = userServiceClient.getUserByUsername(userCreateRequestDto.getUsername(), "true");

		Set<NotiChannel> channels = new HashSet<>();
		channels.add(NotiChannel.WEB);
		channels.add(NotiChannel.EMAIL);

		NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
			user.getId(),
			TargetType.SIGNUP,
			channels,
			NotiType.SIGNUP_USER
		);

		kafkaTemplate.send("notification-topic", notificationRequestDto);

		// notificationServiceClient.createSignupNotification(notificationRequestDto);

		return responseDto;

	}

	public OwnerResponseDto ownerSignUp(OwnerSignUpRequestDto requestDto, MultipartFile file) {

		// 사업자 등록증 업로드
		String uploadedUrl = s3Uploader.upload(file, "business_license");

		String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());

		OwnerCreateRequestDto ownerCreateRequestDto = new OwnerCreateRequestDto(requestDto.getUsername(),
			encryptedPassword, requestDto.getName(), requestDto.getPhoneNumber(), requestDto.getEmail(),
			requestDto.getRole(), requestDto.getBusinessNumber(), uploadedUrl);

		OwnerResponseDto responseDto = userServiceClient.ownerSignUp(ownerCreateRequestDto, "true");

		UserDto user = userServiceClient.getUserByUsername(ownerCreateRequestDto.getUsername(), "true");

		Set<NotiChannel> channels = new HashSet<>();
		channels.add(NotiChannel.WEB);
		channels.add(NotiChannel.EMAIL);

		NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
			user.getId(),
			TargetType.SIGNUP,
			channels,
			NotiType.SIGNUP_OWNER
		);

		kafkaTemplate.send("notification-topic", notificationRequestDto);

		// notificationServiceClient.createSignupNotification(notificationRequestDto);

		return responseDto;

	}

	public LoginResponseDto login(LoginRequestDto requestDto) {

		UserDto user = userServiceClient.getUserByUsername(requestDto.getUsername(), "true");

		if (!BCrypt.checkpw(requestDto.getPassword(), user.getPassword())) {
			throw new InvalidPasswordException();
		}

		String accessToken = tokenProvider.createAccessToken(user);
		String refreshToken = tokenProvider.createRefreshToken(user);

		try {

			redisTemplate.opsForValue().set(
				"refreshToken:" + user.getId(),
				refreshToken,
				tokenProvider.getExpiration(refreshToken),
				TimeUnit.SECONDS
			);

		} catch (Exception e) {
			throw new RuntimeException("Redis 저장 실패", e);
		}

		return new LoginResponseDto(
			accessToken, refreshToken, "Bearer", tokenProvider.getExpiration(accessToken), new UserResponseDto(user));

	}

	public void updatePassword(String username, PasswordUpdateRequestDto requestDto) {

		UserDto user = userServiceClient.getUserByUsername(username, "true");

		if (!BCrypt.checkpw(requestDto.getCurrentPassword(), user.getPassword())) {
			throw new InvalidPasswordException();
		}

		String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());
		userServiceClient.updatePassword(user.getId(), encodedNewPassword, "true");

	}

	public LoginResponseDto reissueToken(String authHeader, String refreshToken) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			// throw new UnauthorizedException();
			throw new IllegalArgumentException();
		}

		String accessToken = authHeader.substring(7);

		if (!tokenProvider.validateToken(refreshToken)) {
			throw new IllegalArgumentException();
		}

		Claims claims = tokenProvider.parseToken(refreshToken);
		String username = claims.getSubject();

		String userId = claims.get("id", String.class);

		String savedRefreshToken = redisTemplate.opsForValue().get("refreshToken:" + userId);
		if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
			throw new IllegalArgumentException();
		}

		UserDto user = userServiceClient.getUserByUsername(username, "true");

		String newAccessToken = tokenProvider.createAccessToken(user);
		String newRefreshToken = tokenProvider.createRefreshToken(user);

		try {

			redisTemplate.opsForValue().set(
				"refreshToken:" + user.getId(),
				refreshToken,
				tokenProvider.getExpiration(refreshToken),
				TimeUnit.SECONDS
			);

		} catch (Exception e) {
			throw new RuntimeException("Redis 저장 실패", e);
		}

		return new LoginResponseDto(
			accessToken, refreshToken, "Bearer", tokenProvider.getExpiration(accessToken), new UserResponseDto(user));


	}

	public void logout(String authHeader) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			// throw new UnauthorizedException();
			throw new IllegalArgumentException();
		}

		String accessToken = authHeader.substring(7);

		String userId;

		try {

			Claims claims = tokenProvider.parseToken(accessToken);
			userId = claims.get("id", String.class);

		} catch (Exception e) {
			throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
		}

		redisTemplate.delete("refreshToken:" + userId);

		redisTemplate.opsForValue().set(
			"blacklist:" + accessToken, "logout", tokenProvider.getExpiration(accessToken), TimeUnit.SECONDS);

	}

}
