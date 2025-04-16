package com.tweaty.reservation.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweaty.reservation.domain.model.Reservation;
import com.tweaty.reservation.domain.repository.ReservationRepository;

public interface JpaReservationRepository
	extends JpaRepository<Reservation, UUID>, ReservationRepository {

}
