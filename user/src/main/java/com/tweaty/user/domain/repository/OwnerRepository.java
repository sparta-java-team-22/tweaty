package com.tweaty.user.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tweaty.user.domain.model.Owner;

public interface OwnerRepository {

	void save(Owner owner);

	Optional<Owner> findById(UUID id);

	Page<Owner> getOwnerList(String key, Pageable pageable, String sortBy, String order);

	Page<Owner> getPendingOwnersList(Pageable pageable, String sortBy, String order);

}