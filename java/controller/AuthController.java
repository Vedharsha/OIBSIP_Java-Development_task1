package com.reservation.onlinebooking.controller;

import com.reservation.onlinebooking.model.User;
import com.reservation.onlinebooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> existingOpt = userRepository.findByUsername(loginUser.getUsername());

        if (existingOpt.isPresent()) {
            User u = existingOpt.get();
            if (passwordEncoder.matches(loginUser.getPassword(), u.getPassword())) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "userId", u.getId(),
                        "role", u.getRole()
                ));
            }
        }
        
        return ResponseEntity.ok(Map.of("status", "fail"));
    }
    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.ok(Map.of("status", "exists"));
        }
        
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("status", "registered"));
    }


    @GetMapping("/reserve")
    public String reservePage() {
        return "reserve.html";
    }

}

