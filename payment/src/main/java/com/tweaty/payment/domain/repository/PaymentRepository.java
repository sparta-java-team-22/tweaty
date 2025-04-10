package com.tweaty.payment.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

	Page<Payment> findByUserIdAndIsDeletedFalseAndStatus(UUID userId, PaymentType status, Pageable pageable);

}
