package com.tweaty.user.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.tweaty.user.domain.model.Owner;
import com.tweaty.user.domain.repository.OwnerRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OwnerRepositoryImpl implements OwnerRepository {

	private final JpaOwnerRepository jpaOwnerRepository;
	private final UserQueryDslRepositoryImpl userQueryDslRepository;

	@Override
	public void save(Owner owner) {
		jpaOwnerRepository.save(owner);
	}

	@Override
	public Optional<Owner> findById(UUID id) {
		return jpaOwnerRepository.findById(id);
	}

	@Override
	public Page<Owner> getOwnerList(String key, Pageable pageable, String sortBy, String order) {
		return userQueryDslRepository.getOwnerList(key, pageable, sortBy, order);
	}

	@Override
	public Page<Owner> getPendingOwnersList(Pageable pageable, String sortBy, String order) {
		return userQueryDslRepository.getPendingOwnersList(pageable, sortBy, order);
	}
}
