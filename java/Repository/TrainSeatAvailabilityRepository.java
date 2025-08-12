package com.reservation.onlinebooking.repository;

import com.reservation.onlinebooking.model.TrainSeatAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TrainSeatAvailabilityRepository extends JpaRepository<TrainSeatAvailability, Long> {
    Optional<TrainSeatAvailability> findByTrainIdAndJourneyDateAndJourneyTime(Integer trainId, LocalDate journeyDate, LocalTime journeyTime);

    List<TrainSeatAvailability> findByJourneyDateBeforeOrJourneyDateEqualsAndJourneyTimeBefore(
            LocalDate beforeDate, LocalDate equalsDate, LocalTime beforeTime);

    Optional<TrainSeatAvailability> findByTrainIdAndJourneyDate(Integer trainId, LocalDate date);
}


