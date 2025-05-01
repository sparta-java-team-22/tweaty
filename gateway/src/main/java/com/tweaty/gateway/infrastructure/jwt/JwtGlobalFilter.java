package com.tweaty.gateway.infrastructure.jwt;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtGlobalFilter implements GlobalFilter {

	private final JwtTokenParser tokenParser;
	private final StringRedisTemplate redisTemplate;

	/**
	 * Intercepts incoming HTTP requests to enforce JWT authentication and token blacklist validation.
	 *
	 * <p>Allows unauthenticated access to specific public or internal endpoints. For all other requests,
	 * validates the presence and format of the Authorization header, checks if the JWT token is blacklisted in Redis,
	 * and parses the token to extract user information. If valid, adds user-related headers to the request and forwards it.
	 * Responds with 403 Forbidden if the Authorization header is missing or invalid, and 401 Unauthorized if the token is blacklisted
	 * or token parsing fails.
	 *
	 * @param exchange the current server web exchange
	 * @param chain the gateway filter chain
	 * @return a {@code Mono<Void>} that completes when request processing is finished
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		String path = exchange.getRequest().getPath().toString();
		String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
		String contentType = exchange.getRequest().getHeaders().getFirst("Content-Type");

		if (path.startsWith("/api/v1/auth/login") || path.startsWith("/api/v1/auth/signup/") || (
			path.startsWith("/api/v1/users/internal") && "true".equals(
				exchange.getRequest().getHeaders().getFirst("internal-request"))) || (
			path.startsWith("/api/v1/auth/signup/owner") && contentType != null && contentType.startsWith(
				"multipart/"))) {
			return chain.filter(exchange);
		}

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
			return exchange.getResponse().setComplete();
		}

		try {
			String token = authHeader.substring(7);

			String isLogout = redisTemplate.opsForValue().get("blacklist:" + token);
			if (isLogout != null) {
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}

			Claims claims = tokenParser.parseToken(token);

			ServerHttpRequest request = exchange.getRequest()
				.mutate()
				.header("X-USER-ID", claims.get("id").toString())
				.header("X-USERNAME", claims.getSubject())
				.header("X-USER-ROLE", claims.get("role").toString())
				.build();

			return chain.filter(exchange.mutate().request(request).build());

		} catch (Exception e) {
			e.printStackTrace();
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
	}
}
