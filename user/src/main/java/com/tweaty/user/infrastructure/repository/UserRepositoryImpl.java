package com.tweaty.user.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.tweaty.user.domain.model.User;
import com.tweaty.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final JpaUserRepository jpaUserRepository;
	private final UserQueryDslRepositoryImpl userQueryDslRepository;

	@Override
	public boolean existsByUsername(String username) {
		return jpaUserRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return jpaUserRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByPhoneNumber(String phoneNumber) {
		return jpaUserRepository.existsByPhoneNumber(phoneNumber);
	}

	@Override
	public void save(User user) {
		jpaUserRepository.save(user);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return jpaUserRepository.findByUsername(username);
	}

	@Override
	public Optional<User> findById(UUID userId) {
		return jpaUserRepository.findById(userId);
	}

	@Override
	public Page<User> getUserList(String key, Pageable pageable, String sortBy, String order) {
		return userQueryDslRepository.getUserList(key, pageable, sortBy, order);
	}
}

