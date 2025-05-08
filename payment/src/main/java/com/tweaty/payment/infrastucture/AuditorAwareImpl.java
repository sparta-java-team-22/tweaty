package com.tweaty.payment.infrastucture;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component("auditorAwareImpl")
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

	private final HttpServletRequest request;

	@Override
	public Optional<String> getCurrentAuditor() {
		try {
			String requestUserId = request.getHeader("X-USER-ID");

			return Optional.ofNullable(requestUserId);
		} catch(Exception e) {
			return Optional.of("system");

		}
	}

}
