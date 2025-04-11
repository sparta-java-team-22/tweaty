package com.tweaty.payment.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tweaty.payment.domain.entity.Refund;

@Repository
public interface RefundRepository extends JpaRepository<Refund, UUID> {


}
