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

	/**
	 * Registers a new user, encrypts their password, and sends a signup notification via Kafka.
	 *
	 * @param requestDto the user signup request containing registration details
	 * @return the response containing the registered user's information
	 */
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

	/**
	 * Registers a new owner account, uploads the business license file to S3, and sends a signup notification via Kafka.
	 *
	 * @param requestDto the owner signup request containing user and business details
	 * @param file the business license file to be uploaded
	 * @return the response containing the registered owner's information
	 */
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

	/**
	 * Authenticates a user and issues JWT access and refresh tokens.
	 *
	 * Validates the provided credentials, generates tokens upon successful authentication, stores the refresh token in Redis with its expiration, and returns a response containing tokens and user information.
	 *
	 * @param requestDto the login request containing username and password
	 * @return a response with access token, refresh token, token type, expiration, and user details
	 * @throws InvalidPasswordException if the password is incorrect
	 * @throws RuntimeException if storing the refresh token in Redis fails
	 */
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

	/**
	 * Updates a user's password after verifying the current password.
	 *
	 * @param username the username of the user whose password is to be updated
	 * @param requestDto contains the current and new passwords for the update
	 * @throws InvalidPasswordException if the current password does not match the user's existing password
	 */
	public void updatePassword(String username, PasswordUpdateRequestDto requestDto) {

		UserDto user = userServiceClient.getUserByUsername(username, "true");

		if (!BCrypt.checkpw(requestDto.getCurrentPassword(), user.getPassword())) {
			throw new InvalidPasswordException();
		}

		String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());
		userServiceClient.updatePassword(user.getId(), encodedNewPassword, "true");

	}

	/**
	 * Issues new access and refresh tokens after validating the provided refresh token and access token header.
	 *
	 * Validates the format of the authorization header and the refresh token, checks the refresh token against the stored value in Redis, and retrieves the user information. Generates new tokens, updates the refresh token in Redis, and returns a response containing the new tokens and user details.
	 *
	 * @param authHeader the HTTP Authorization header containing the access token in "Bearer" format
	 * @param refreshToken the JWT refresh token to be validated and reissued
	 * @return a LoginResponseDto containing the new access token, refresh token, token type, expiration, and user information
	 * @throws IllegalArgumentException if the authorization header or refresh token is invalid, or if the refresh token does not match the stored value
	 * @throws RuntimeException if storing the new refresh token in Redis fails
	 */
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

	/**
	 * Logs out a user by invalidating their refresh token and blacklisting the access token.
	 *
	 * @param authHeader the HTTP Authorization header containing the Bearer access token
	 * @throws IllegalArgumentException if the authorization header is missing, improperly formatted, or the token is invalid
	 */
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
