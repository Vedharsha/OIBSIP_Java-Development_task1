package com.reservation.onlinebooking.service;

import com.reservation.onlinebooking.model.TrainSeatAvailability;
import com.reservation.onlinebooking.repository.TrainSeatAvailabilityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduledSeatResetService {

    private final TrainSeatAvailabilityRepository trainSeatAvailabilityRepository;

    public ScheduledSeatResetService(TrainSeatAvailabilityRepository trainSeatAvailabilityRepository) {
        this.trainSeatAvailabilityRepository = trainSeatAvailabilityRepository;
    }

    // Runs every minute to check journeys that are in the past
    @Scheduled(fixedRate = 60000) // 1 minute
    public void checkPastJourneys() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // Get all past journeys using your existing repo method
        List<TrainSeatAvailability> pastJourneys =
                trainSeatAvailabilityRepository.findByJourneyDateBeforeOrJourneyDateEqualsAndJourneyTimeBefore(
                        today, today, now
                );

        for (TrainSeatAvailability journey : pastJourneys) {
            // Example: Reduce available seats to 0 after departure
            journey.setAvailableSeats(0);
            trainSeatAvailabilityRepository.save(journey);

            System.out.println("Updated journey as departed: Train ID " + journey.getTrainId() +
                    " Date " + journey.getJourneyDate() + " Time " + journey.getJourneyTime());
        }
    }
}
