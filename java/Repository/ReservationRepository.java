package com.reservation.onlinebooking.repository;

import com.reservation.onlinebooking.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUserId(Long userId);
}
