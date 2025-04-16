package com.tweaty.auth.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.tweaty.auth.application.dto.OwnerCreateRequestDto;
import com.tweaty.auth.application.dto.OwnerResponseDto;
import com.tweaty.auth.application.dto.UserCreateRequestDto;
import com.tweaty.auth.application.dto.UserDto;
import com.tweaty.auth.application.dto.UserResponseDto;

@FeignClient(name = "user-service")
public interface UserServiceClient {

	@PostMapping("/api/v1/users/internal/signup/user")
	UserResponseDto userSignUp(@RequestBody UserCreateRequestDto requestDto,
		@RequestHeader("internal-request") String header);

	@PostMapping("/api/v1/users/internal/signup/owner")
	OwnerResponseDto ownerSignUp(@RequestBody OwnerCreateRequestDto requestDto,
		@RequestHeader("internal-request") String header);

	@GetMapping("/api/v1/users/internal")
	UserDto getUserByUsername(@RequestParam("username") String username,
		@RequestHeader("internal-request") String header);

	@PutMapping("/api/v1/users/internal/{id}/password")
	void updatePassword(@PathVariable("id") UUID id, @RequestBody String encodedPassword,
		@RequestHeader("internal-request") String header);
}
