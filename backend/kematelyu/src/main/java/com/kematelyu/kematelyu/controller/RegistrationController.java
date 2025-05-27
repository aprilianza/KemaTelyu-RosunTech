package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.RegistrationListDTO;
import com.kematelyu.kematelyu.exception.ForbiddenException;
import com.kematelyu.kematelyu.exception.UnauthorizedException;
import com.kematelyu.kematelyu.model.*;
import com.kematelyu.kematelyu.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Endpoint registration mahasiswa.
 *
 *  GET /api/registrations/myregist
 *  – Hanya bisa di-hit oleh bearer token dengan role MAHASISWA.
 *  – Mengembalikan list seluruh pendaftaran milik mahasiswa tsb.
 */
@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final UserRepository         userRepo;
    private final RegistrationRepository regRepo;
    private final CertificateRepository  certRepo;

    public RegistrationController(UserRepository userRepo,
                                  RegistrationRepository regRepo,
                                  CertificateRepository certRepo) {
        this.userRepo = userRepo;
        this.regRepo  = regRepo;
        this.certRepo = certRepo;
    }

    /* ====================================================================== */
    /* ===========================  ENDPOINT  =============================== */
    /* ====================================================================== */

    @GetMapping("/myregist")
    public ResponseEntity<List<RegistrationListDTO>> getMyRegistrations() {

        /* ------------ Dapatkan userId dari token ------------ */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null)
            throw new UnauthorizedException("Token tidak valid");

        Long userId;
        try {
            userId = Long.parseLong(auth.getPrincipal().toString());
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("Token tidak valid");
        }

        /* ------------ Validasi role = MAHASISWA ------------ */
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User tidak ditemukan"));

        if (!(user instanceof Mahasiswa mahasiswa))
            throw new ForbiddenException("Hanya mahasiswa yang dapat mengakses resource ini");

        /* ------------ Fetch data pendaftaran ------------ */
        // ambil semua sertifikat milik mahasiswa: eventId -> certificateId
        Map<Long, Long> certMap = certRepo.findByMahasiswa(mahasiswa).stream()
                .collect(Collectors.toMap(c -> c.getEvent().getId(), Certificate::getId));

        // ambil seluruh registration milik mahasiswa
        List<RegistrationListDTO> result = regRepo.findAll().stream()
                .filter(r -> r.getMahasiswa().getId().equals(userId))
                .map(r -> new RegistrationListDTO(
                        r.getEvent().getId(),
                        r.getEvent().getTitle(),
                        r.getEvent().getDescription(),
                        r.getDate(),
                        r.getStatus().name(),
                        certMap.getOrDefault(r.getEvent().getId(), null)
                ))
                .toList();

        return ResponseEntity.ok(result);
    }
}
