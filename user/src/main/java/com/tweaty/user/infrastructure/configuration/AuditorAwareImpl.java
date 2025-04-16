package com.tweaty.user.infrastructure.configuration;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<UUID> {

	private final HttpServletRequest request;

	@Override
	public Optional<UUID> getCurrentAuditor() {

		String requestUserId = request.getHeader("id");
		UUID userId = null;

		if (requestUserId != null && !requestUserId.isBlank()) {
			userId = UUID.fromString(requestUserId);
		}

		return Optional.ofNullable(userId);
	}

}
