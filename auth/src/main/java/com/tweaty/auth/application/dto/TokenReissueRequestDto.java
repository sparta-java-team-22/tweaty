package com.tweaty.auth.application.dto;

import lombok.Getter;

@Getter
public class TokenReissueRequestDto {
	private String refreshToken;
}
