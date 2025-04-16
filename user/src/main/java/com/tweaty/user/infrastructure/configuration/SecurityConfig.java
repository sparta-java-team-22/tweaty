package com.tweaty.user.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tweaty.user.infrastructure.filter.PreAuthHeaderFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final PreAuthHeaderFilter preAuthHeaderFilter;

	public SecurityConfig(PreAuthHeaderFilter preAuthHeaderFilter) {
		this.preAuthHeaderFilter = preAuthHeaderFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http.addFilterBefore(preAuthHeaderFilter, UsernamePasswordAuthenticationFilter.class)
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(form -> form.disable())
			.httpBasic(httpBasic -> httpBasic.disable())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/users/internal")
				.permitAll()
				.requestMatchers("/api/v1/users/internal/**")
				.permitAll()
				.anyRequest()
				.authenticated())
			.build();
	}
}