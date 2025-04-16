package com.tweaty.auth.infrastructure.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final AuthTokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String path = request.getRequestURI();

		if (path.equals("/api/v1/auth/login") || path.startsWith("/api/v1/auth/signup")) {
			filterChain.doFilter(request, response);
			return;
		}

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {

			String token = authHeader.substring(7);

			try {

				Claims claims = tokenProvider.parseToken(token);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					claims.getSubject(), null, AuthorityUtils.createAuthorityList(claims.get("role").toString()));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {

				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
