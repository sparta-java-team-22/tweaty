package com.tweaty.reservation.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweaty.reservation.domain.model.ReservationSchedule;
import com.tweaty.reservation.domain.repository.ReservationScheduleRepository;

public interface JpaReservationScheduleRepository
	extends JpaRepository<ReservationSchedule, UUID>, ReservationScheduleRepository {

}
