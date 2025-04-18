package com.tweaty.user.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweaty.user.domain.model.User;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);

	Optional<User> findByUsername(String username);
}