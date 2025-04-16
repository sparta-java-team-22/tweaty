package com.tweaty.auth.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tweaty.auth.application.dto.UserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthTokenProvider {

	@Value("${jwt.secret}")
	private String secretKey;
	private final long accessTokenValidTime = 60 * 60 * 1000L; //1시간

	public String createAccessToken(UserDto user) {

		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

		Date now = new Date();

		return Jwts.builder()
			.claim("id", user.getId())
			.setSubject(user.getUsername())
			.claim("role", user.getRole())
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + accessTokenValidTime))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public Claims parseToken(String token) {

		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

}
