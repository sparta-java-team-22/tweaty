package com.tweaty.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {

	private String accessToken;
	private String refreshToken;
	private String tokenType;
	private long expiresIn;
	private UserResponseDto user;

}
