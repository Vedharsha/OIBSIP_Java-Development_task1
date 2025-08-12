package com.reservation.onlinebooking.controller;

import com.reservation.onlinebooking.model.Train;
import com.reservation.onlinebooking.model.TrainSeatAvailability;
import com.reservation.onlinebooking.repository.TrainRepository;
import com.reservation.onlinebooking.repository.TrainSeatAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TrainSeatAvailabilityController {

    @Autowired
    private TrainSeatAvailabilityRepository seatAvailabilityRepository;

    @Autowired
    private TrainRepository trainRepository;

    @GetMapping("/trainseats")
    public ResponseEntity<?> getSeatAvailability(
            @RequestParam Integer trainId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Optional<TrainSeatAvailability> opt = seatAvailabilityRepository.findByTrainIdAndJourneyDate(trainId, date);

        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        } else {
            // Create a new seat availability record with full capacity from Train table
            Train train = trainRepository.findById(trainId)
                    .orElseThrow(() -> new IllegalArgumentException("Train not found"));

            TrainSeatAvailability tsa = new TrainSeatAvailability();
            tsa.setTrainId(trainId);
            tsa.setJourneyDate(date);
            tsa.setJourneyTime(train.getEstimatedReachTime()); // Assuming journey time is in Train entity
            tsa.setAvailableSeats(train.getAvailableSeats());  // Full capacity from Train table

            seatAvailabilityRepository.save(tsa);

            return ResponseEntity.ok(tsa);
        }
    }
}
