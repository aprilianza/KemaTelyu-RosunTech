package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.RegistrationListDTO;
import com.kematelyu.kematelyu.exception.ForbiddenException;
import com.kematelyu.kematelyu.exception.UnauthorizedException;
import com.kematelyu.kematelyu.model.*;
import com.kematelyu.kematelyu.model.Registration.Status;
import com.kematelyu.kematelyu.repository.*;
import com.kematelyu.kematelyu.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Endpoint registration mahasiswa.
 *
 * GET /api/registrations/myregist – list registrasi milik mahasiswa
 * DELETE /api/registrations/{id} – batalkan registrasi (hanya PENDING, milik
 * sendiri)
 * PATCH /api/registrations/{id}/approve – approve (STAFF)
 * PATCH /api/registrations/{id}/reject – reject (STAFF)
 */
@RestController
@RequestMapping("/api/registrations")
@CrossOrigin
public class RegistrationController {

    private final UserRepository userRepo;
    private final RegistrationRepository regRepo;
    private final CertificateRepository certRepo;
    private final RegistrationService registrationService;

    public RegistrationController(UserRepository userRepo,
            RegistrationRepository regRepo,
            CertificateRepository certRepo,
            RegistrationService registrationService) {
        this.userRepo = userRepo;
        this.regRepo = regRepo;
        this.certRepo = certRepo;
        this.registrationService = registrationService;
    }

    /*
     * ======================================================================
     * LIST REGISTRASI MILIK MAHASISWA
     */

    @GetMapping("/myregist")
    public ResponseEntity<List<RegistrationListDTO>> getMyRegistrations() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null)
            throw new UnauthorizedException("Token tidak valid");

        Long userId;
        try {
            userId = Long.parseLong(auth.getPrincipal().toString());
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("Token tidak valid");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User tidak ditemukan"));

        if (!(user instanceof Mahasiswa mahasiswa))
            throw new ForbiddenException("Hanya mahasiswa yang dapat mengakses resource ini");

        /* Semua sertifikat milik mahasiswa: eventId -> certificateId */
        Map<Long, Long> certMap = certRepo.findByMahasiswa(mahasiswa).stream()
                .collect(Collectors.toMap(c -> c.getEvent().getId(), Certificate::getId));

        /* Registrasi milik mahasiswa */
        List<RegistrationListDTO> result = regRepo.findAll().stream()
                .filter(r -> r.getMahasiswa().getId().equals(userId))
                .map(r -> new RegistrationListDTO(
                        r.getId(), // registrationId
                        r.getEvent().getId(),
                        r.getEvent().getTitle(),
                        r.getEvent().getDescription(),
                        r.getDate(),
                        r.getStatus().name(),
                        certMap.getOrDefault(r.getEvent().getId(), null)))
                .toList();

        return ResponseEntity.ok(result);
    }

    /*
     * ======================================================================
     * MAHASISWA BATALKAN REGISTRASINYA SENDIRI (hanya PENDING)
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegistration(@PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null)
            throw new UnauthorizedException("Token tidak valid");

        Long userId;
        try {
            userId = Long.parseLong(auth.getPrincipal().toString());
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("Token tidak valid");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User tidak ditemukan"));

        if (!(user instanceof Mahasiswa mahasiswa))
            throw new ForbiddenException("Hanya mahasiswa yang dapat menghapus pendaftaran");

        Registration reg = regRepo.findById(id)
                .orElseThrow(() -> new ForbiddenException("Registrasi tidak ditemukan"));

        if (!reg.getMahasiswa().getId().equals(mahasiswa.getId()))
            throw new ForbiddenException("Tidak boleh menghapus pendaftaran milik orang lain");

        if (reg.getStatus() != Status.PENDING)
            throw new ForbiddenException("Pendaftaran hanya dapat dibatalkan apabila masih berstatus PENDING");

        regRepo.delete(reg);
        return ResponseEntity.ok(Map.of("message", "Registrasi berhasil dibatalkan"));
    }

    /*
     * ======================================================================
     * STAFF APPROVE / REJECT REGISTRASI
     */

    @PatchMapping("/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveRegistration(@PathVariable Long id) {

        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();
        if (!"ROLE_STAFF".equals(role))
            throw new ForbiddenException("Hanya staff yang boleh meng-approve pendaftaran.");

        Registration r = registrationService.approveParticipant(id);
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "status", "OK",
                "message", "Berhasil approve peserta: " + r.getMahasiswa().getName()));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectRegistration(@PathVariable Long id) {

        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();
        if (!"ROLE_STAFF".equals(role))
            throw new ForbiddenException("Hanya staff yang boleh menolak pendaftaran.");

        Registration r = registrationService.rejectParticipant(id);
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "status", "OK",
                "message", "Pendaftaran ditolak untuk: " + r.getMahasiswa().getName()));
    }
}
