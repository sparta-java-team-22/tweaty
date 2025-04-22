package com.tweaty.user.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tweaty.user.domain.model.User;

import domain.Role;

public interface UserRepository {

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);

	void save(User user);

	Optional<User> findByUsername(String username);

	Optional<User> findById(UUID userId);

	Page<User> getUserList(String key, Role role, Pageable pageable, String sortBy, String order);
}