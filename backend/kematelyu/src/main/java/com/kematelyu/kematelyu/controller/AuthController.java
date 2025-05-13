package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.LoginRequest;
import com.kematelyu.kematelyu.dto.LoginResponse;
import com.kematelyu.kematelyu.model.User;
import com.kematelyu.kematelyu.repository.UserRepository;
import com.kematelyu.kematelyu.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwt;

    public AuthController(UserRepository userRepo, JwtUtil jwt) {
        this.userRepo = userRepo;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        User user = userRepo.findByEmail(req.getEmail()).orElse(null);
        if (user == null || !user.getPassword().equals(req.getPassword())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Email / password salah");
        }

        String token = jwt.generateToken(user.getId(), user.getRole());
        return ResponseEntity.ok(new LoginResponse("Login berhasil", user, token));
    }
}
