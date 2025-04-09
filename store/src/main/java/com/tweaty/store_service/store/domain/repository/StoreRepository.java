package com.tweaty.store_service.store.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tweaty.store_service.store.domain.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

	@Override
	Optional<Store> findById(UUID uuid);
}
