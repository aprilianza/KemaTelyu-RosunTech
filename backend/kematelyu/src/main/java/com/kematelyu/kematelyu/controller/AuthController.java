package com.kematelyu.kematelyu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kematelyu.kematelyu.dto.LoginRequest;
import com.kematelyu.kematelyu.dto.LoginResponse;
import com.kematelyu.kematelyu.model.User;
import com.kematelyu.kematelyu.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                                      .orElse(null);

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email tidak ditemukan");
            }

            if (!user.getPassword().equals(request.getPassword())) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Password salah");
            }

            return ResponseEntity.ok(new LoginResponse("Login berhasil", user));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan server: " + e.getMessage(), e);
        }
    }
}
