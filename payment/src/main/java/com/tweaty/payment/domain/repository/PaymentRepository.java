package com.tweaty.payment.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tweaty.payment.domain.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
