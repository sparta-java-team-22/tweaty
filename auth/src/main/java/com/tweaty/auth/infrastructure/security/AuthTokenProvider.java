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
	private final long refreshTokenValidTime = 60L * 60 * 24 * 14 * 1000; /**
	 * Generates a JWT access token containing the user's ID, username, and role, signed with the secret key and valid for one hour.
	 *
	 * @param user the user for whom the access token is generated
	 * @return a signed JWT access token as a string
	 */

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

	/**
	 * Generates a JWT refresh token for the specified user with a 14-day expiration.
	 *
	 * The token includes the user's ID and username as claims and is signed using the configured secret key.
	 *
	 * @param user the user for whom the refresh token is generated
	 * @return a signed JWT refresh token string
	 */
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

	/**
	 * Parses a JWT token and returns its claims.
	 *
	 * @param token the JWT token to parse
	 * @return the claims contained within the token
	 */
	public Claims parseToken(String token) {

		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	/**
	 * Returns the remaining validity period of a JWT token in seconds.
	 *
	 * @param token the JWT token to check
	 * @return the number of seconds until the token expires
	 */
	public long getExpiration(String token) {

		Claims claims = parseToken(token);
		Date expiration = claims.getExpiration();

		return (expiration.getTime() - System.currentTimeMillis()) / 1000;

	}

	/**
	 * Validates a JWT token by checking its signature and expiration date.
	 *
	 * @param token the JWT token to validate
	 * @return true if the token is valid and not expired; false otherwise
	 */
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
