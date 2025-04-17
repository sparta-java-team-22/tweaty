package com.tweaty.auth.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthorizationFilter jwtFilter) throws
		Exception {
		return http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/login", "/api/v1/auth/signup/**")
				.permitAll()
				.requestMatchers("/api/v1/auth/password")
				.authenticated()
				.anyRequest()
				.authenticated())
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

}
