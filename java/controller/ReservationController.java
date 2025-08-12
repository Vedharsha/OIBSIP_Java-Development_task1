package com.reservation.onlinebooking.controller;

import com.reservation.onlinebooking.model.Reservation;
import com.reservation.onlinebooking.service.ReservationService;
import com.reservation.onlinebooking.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired private ReservationService reservationService;
    @Autowired private ReservationRepository reservationRepository;

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(String.valueOf(payload.get("userId")));
            Integer trainId = Integer.valueOf(String.valueOf(payload.get("trainId")));
            int seats = Integer.parseInt(String.valueOf(payload.get("seatsReserved")));
            String from = (String) payload.get("fromPlace");
            String to = (String) payload.get("toPlace");
            LocalDate date = LocalDate.parse((String) payload.get("dateOfJourney"));

            Reservation r = reservationService.book(userId, trainId, date, seats, from, to);
            return ResponseEntity.ok(Map.of("status","booked","pnr", r.getPnr()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("status","error","message", ex.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> myBookings(@PathVariable Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @PostMapping("/cancel/{pnr}")
    public ResponseEntity<?> cancel(@PathVariable Integer pnr) {
        try {
            Reservation r = reservationService.cancel(pnr);
            return ResponseEntity.ok(Map.of("status","cancelled","pnr", r.getPnr()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("status","error","message", ex.getMessage()));
        }
    }


}
