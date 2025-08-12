package com.reservation.onlinebooking.repository;

import com.reservation.onlinebooking.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train, Integer> {
    // optional helpers if needed
}
