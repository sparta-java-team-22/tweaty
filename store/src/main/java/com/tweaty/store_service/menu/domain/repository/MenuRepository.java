package com.tweaty.store_service.menu.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tweaty.store_service.menu.domain.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {

}
