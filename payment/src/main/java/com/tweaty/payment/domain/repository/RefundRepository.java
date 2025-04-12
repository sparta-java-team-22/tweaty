package com.tweaty.payment.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;
import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.domain.entity.RefundType;

@Repository
public interface RefundRepository extends JpaRepository<Refund, UUID> {

	Page<Refund> findByUserIdAndStatus(UUID userId, RefundType status, Pageable pageable);


}
