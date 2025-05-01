package com.tweaty.auth.domain.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14)
public class RefreshToken implements Serializable {

	@Id
	private UUID id;

	private String username;

	@Indexed
	private String accessToken;

	private String refreshToken;

	public void updateAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
