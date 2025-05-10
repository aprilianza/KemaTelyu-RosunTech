package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.LoginRequest;
import com.kematelyu.kematelyu.dto.LoginResponse;
import com.kematelyu.kematelyu.model.User;
import com.kematelyu.kematelyu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body("Email tidak ditemukan");
        }

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Password salah");
        }

        return ResponseEntity.ok(new LoginResponse("Login berhasil", user));
    }
}
