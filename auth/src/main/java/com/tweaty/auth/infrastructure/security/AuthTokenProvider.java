package com.tweaty.auth.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tweaty.auth.application.dto.UserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
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
	private final long refreshTokenValidTime = 60L * 60 * 24 * 14 * 1000; // 14일

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

	public String createRefreshToken(UserDto user) {

		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		Date now = new Date();

		return Jwts.builder()
			.claim("id", user.getId())
			.setSubject(user.getUsername())
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + refreshTokenValidTime))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public Claims parseToken(String token) {

		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public long getExpiration(String token) {

		Claims claims = parseToken(token);
		Date expiration = claims.getExpiration();

		return (expiration.getTime() - System.currentTimeMillis()) / 1000;

	}

	public boolean validateToken(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
			Jws<Claims> claimsJws = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);

			return !claimsJws.getBody().getExpiration().before(new Date());
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

}
