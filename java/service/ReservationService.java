package com.reservation.onlinebooking.service;

import com.reservation.onlinebooking.model.Reservation;
import com.reservation.onlinebooking.model.Train;
import com.reservation.onlinebooking.model.TrainSeatAvailability;
import com.reservation.onlinebooking.repository.ReservationRepository;
import com.reservation.onlinebooking.repository.TrainRepository;
import com.reservation.onlinebooking.repository.TrainSeatAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TrainSeatAvailabilityRepository seatAvailabilityRepository;

    /**
     * Book a reservation and decrement seats.
     */
    @Transactional
    public Reservation book(Long userId, Integer trainId, LocalDate date, int seats, String from, String to) {
    Train train = trainRepository.findById(trainId)
        .orElseThrow(() -> new IllegalArgumentException("Train not found"));

    if (seats <= 0) throw new IllegalArgumentException("Seats must be > 0");

    LocalTime journeyTime = train.getEstimatedReachTime(); // Assume getter exists

    System.out.println("Booking: trainId=" + trainId + ", date=" + date + ", journeyTime=" + journeyTime + ", seats=" + seats);

    TrainSeatAvailability seatAvailability = seatAvailabilityRepository
        .findByTrainIdAndJourneyDateAndJourneyTime(trainId, date, journeyTime)
        .orElseGet(() -> {
            System.out.println("No seat record found, creating new for trainId=" + trainId + ", date=" + date + ", journeyTime=" + journeyTime);
            TrainSeatAvailability tsa = new TrainSeatAvailability();
            tsa.setTrainId(trainId);
            tsa.setJourneyDate(date);
            tsa.setJourneyTime(journeyTime);
            tsa.setAvailableSeats(train.getAvailableSeats()); // start full seats
            return tsa;
        });

    System.out.println("Current available seats: " + seatAvailability.getAvailableSeats());

    if (seatAvailability.getAvailableSeats() < seats)
        throw new IllegalArgumentException("Not enough seats available for selected date and time");

    // Decrement seats
    seatAvailability.setAvailableSeats(seatAvailability.getAvailableSeats() - seats);
    seatAvailabilityRepository.save(seatAvailability);
    System.out.println("Seats after booking: " + seatAvailability.getAvailableSeats());

    // Create and save reservation
    Reservation r = new Reservation();
    r.setUserId(userId);
    r.setTrainId(trainId);
    r.setDateOfJourney(date);
    r.setSeatsReserved(seats);
    r.setFromPlace(from);
    r.setToPlace(to);
    r.setStatus("BOOKED");

    return reservationRepository.save(r);
    }

    /**
     * Cancel a reservation and increment seats.
     */
    @Transactional
    public Reservation cancel(Integer pnr) {
        Reservation r = reservationRepository.findById(pnr)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (!"BOOKED".equalsIgnoreCase(r.getStatus())) {
            throw new IllegalArgumentException("Already cancelled");
        }

        Train train = trainRepository.findById(r.getTrainId())
                .orElseThrow(() -> new IllegalArgumentException("Train not found"));

        LocalTime journeyTime = train.getEstimatedReachTime();

        TrainSeatAvailability seatAvailability = seatAvailabilityRepository
                .findByTrainIdAndJourneyDateAndJourneyTime(r.getTrainId(), r.getDateOfJourney(), journeyTime)
                .orElseThrow(() -> new IllegalArgumentException("Seat availability record not found"));

        // Add back seats on cancellation
        seatAvailability.setAvailableSeats(seatAvailability.getAvailableSeats() + r.getSeatsReserved());
        seatAvailabilityRepository.save(seatAvailability);

        r.setStatus("CANCELLED");
        return reservationRepository.save(r);
    }

    /**
     * Check and update reservations after journey completion.
     * This can be called manually or scheduled with @Scheduled in another class.
     */
    @Transactional
    public void completeJourneysIfTimePassed() {
        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation r : reservations) {
            if ("BOOKED".equalsIgnoreCase(r.getStatus())) {
                Train train = trainRepository.findById(r.getTrainId()).orElse(null);
                if (train == null) continue;

                LocalDateTime reachDateTime = LocalDateTime.of(r.getDateOfJourney(), train.getEstimatedReachTime());

                // If journey completed and not cancelled
                if (LocalDateTime.now().isAfter(reachDateTime)) {
                    r.setStatus("COMPLETED");
                    reservationRepository.save(r);
                    // Seats are already considered consumed, so no increment/decrement here
                }
            }
        }
    }
}
