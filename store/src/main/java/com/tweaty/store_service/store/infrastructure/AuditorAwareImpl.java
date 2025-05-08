package com.tweaty.store_service.store.infrastructure;

import java.util.Optional;
import java.util.UUID;

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
		String requestUserId = request.getHeader("X-USER-ID");

		return Optional.ofNullable(requestUserId);
	}

}
