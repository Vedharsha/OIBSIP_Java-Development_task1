package com.reservation.onlinebooking.controller;

import com.reservation.onlinebooking.model.Train;
import com.reservation.onlinebooking.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@CrossOrigin(origins = "*")
public class TrainController {
    @Autowired private TrainRepository trainRepository;

    @GetMapping
    public List<Train> all() {
        return trainRepository.findAll();
    }

    @GetMapping("/{id}")
    public Train get(@PathVariable Integer id) {
        return trainRepository.findById(id).orElse(null);
    }
}
