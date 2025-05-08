package com.tweaty.payment.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;

import jakarta.persistence.LockModeType;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

	Page<Payment> findByUserIdAndStatus(UUID userId, PaymentType status, Pageable pageable);

	@Query("""
	SELECT p FROM Payment p
	WHERE p.userId = :userId 
	  AND p.reservationId = :reservationId 
	ORDER BY p.createdAt DESC
""")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Payment> findExistingForUpdate(@Param("userId") UUID userId, @Param("reservationId") UUID reservationId);

	Boolean existsByUserIdAndReservationId(UUID userId, UUID reservationId);



	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Payment p WHERE p.id = :paymentId")
	Optional<Payment> findByIdForUpdate(@Param("paymentId") UUID paymentId);
}
