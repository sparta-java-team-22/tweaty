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

	/**
	 * Updates the access token associated with this refresh token entity.
	 *
	 * @param accessToken the new access token to set
	 */
	public void updateAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Updates the refresh token value for this entity.
	 *
	 * @param refreshToken the new refresh token to set
	 */
	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
