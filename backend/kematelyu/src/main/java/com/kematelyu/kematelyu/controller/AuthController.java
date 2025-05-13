package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.LoginRequest;
import com.kematelyu.kematelyu.dto.LoginResponse;
import com.kematelyu.kematelyu.model.User;
import com.kematelyu.kematelyu.repository.UserRepository;
import com.kematelyu.kematelyu.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwt;

    public AuthController(UserRepository userRepo, JwtUtil jwt) {
        this.userRepo = userRepo;
        this.jwt = jwt;
    }

    /* ---------- LOGIN ---------- */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        User user = userRepo.findByEmail(req.getEmail()).orElse(null);
        if (user == null || !user.getPassword().equals(req.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email / password salah");
        }
        String token = jwt.generateToken(user.getId(), user.getRole());
        return ResponseEntity.ok(new LoginResponse("Login berhasil", user, token));
    }

    /* ---------- CURRENT USER ---------- */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token missing");
        }

        String token = authHeader.substring(7);
        Claims claims = jwt.parse(token).getBody();
        Long userId = Long.parseLong(claims.getSubject());

        return userRepo.findById(userId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }
}
