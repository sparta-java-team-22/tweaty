package com.tweaty.store_service.store.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tweaty.store_service.store.domain.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

	Page<Store> findByIsDeletedIsFalse(Pageable pageable);

	@Query("SELECT s FROM Store s " +
		"WHERE s.isDeleted = false " +
		"AND LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
		"AND (" +
		"  (:isReservation = TRUE AND :isWaiting = TRUE AND s.isReservation = TRUE AND s.isWaiting = TRUE) " +
		"  OR (:isReservation = TRUE AND :isWaiting = FALSE AND s.isReservation = TRUE) " +
		"  OR (:isReservation = FALSE AND :isWaiting = TRUE AND s.isWaiting = TRUE) " +
		"  OR (:isReservation = FALSE AND :isWaiting = FALSE) " +
		")")
	Page<Store> searchStoreList(@Param("name") String name, @Param("isReservation") Boolean isReservation, @Param("isWaiting") Boolean isWaiting, Pageable pageable
	);
}
