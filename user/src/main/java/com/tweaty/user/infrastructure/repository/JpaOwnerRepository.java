package com.tweaty.user.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweaty.user.domain.model.Owner;

public interface JpaOwnerRepository extends JpaRepository<Owner, UUID> {
}