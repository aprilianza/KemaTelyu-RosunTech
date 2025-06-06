package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.CertificateDTO;
import com.kematelyu.kematelyu.exception.ForbiddenException;
import com.kematelyu.kematelyu.exception.NotFoundException;
import com.kematelyu.kematelyu.exception.UnauthorizedException;
import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Mahasiswa;
import com.kematelyu.kematelyu.service.CertificateService;
import com.kematelyu.kematelyu.util.CertificatePdfGenerator;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.Normalizer;
import java.util.*;

/**
 * Endpoint sertifikat (hanya Mahasiswa).
 *
 * GET /api/certificates – list sertifikat milik user login
 * GET /api/certificates/{id}/download – unduh PDF (konten‐saja, TANPA header
 * filename)
 */
@RestController
@RequestMapping("/api/certificates")
@CrossOrigin
public class CertificateController {

    private final CertificateService certService;

    public CertificateController(CertificateService certService) {
        this.certService = certService;
    }

    /* -------------------- LIST -------------------- */
    @GetMapping
    public ResponseEntity<Map<String, Object>> list(Authentication auth) {
        Long userId = extractUserId(auth);

        // pastikan akun MAHASISWA
        if (!hasRole(auth, "ROLE_MAHASISWA"))
            throw new ForbiddenException("Hanya akun MAHASISWA yang boleh mengakses sertifikat");

        List<CertificateDTO> dtoList = certService.getCertificatesByMahasiswaId(userId)
                .stream()
                .map(c -> {
                    Event e = c.getEvent();
                    Mahasiswa m = c.getMahasiswa();
                    return new CertificateDTO(
                            c.getId(), m.getNim(),
                            e.getId(), e.getTitle(),
                            e.getFotoPath(), c.getIssueDate());
                })
                .toList();

        return ResponseEntity.ok(
                Map.of("code", 200, "status", "OK", "message", dtoList));
    }

    /* -------------------- DOWNLOAD (PDF only) -------------------- */
    @GetMapping("{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id, Authentication auth)
            throws IOException {

        Long userId = extractUserId(auth);

        // pastikan akun MAHASISWA
        if (!hasRole(auth, "ROLE_MAHASISWA"))
            throw new ForbiddenException("Hanya akun MAHASISWA yang boleh mengunduh sertifikat");

        // pastikan sertifikat ada
        Certificate cert;
        try {
            cert = certService.findById(id);
        } catch (IllegalArgumentException ex) {
            throw new NotFoundException("Certificate tidak ditemukan");
        }

        // pastikan sertifikat milik user
        if (!Objects.equals(cert.getMahasiswa().getId(), userId))
            throw new ForbiddenException("Certificate tidak dimiliki oleh pengguna saat ini");

        /* generate PDF di memori */
        byte[] pdf = CertificatePdfGenerator.generate(cert);

        // pastikan Content-Length tidak 0 agar Chrome tidak membuat file 0 B
        ByteArrayResource body = new ByteArrayResource(pdf) {
            @Override
            public long contentLength() {
                return pdf.length;
            }
        };

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_PDF);
        h.setContentLength(pdf.length);

        /*
         * TIDAK ADA Content-Disposition dan TIDAK ADA header custom lain.
         * FE akan menentukan nama file sendiri.
         */
        return new ResponseEntity<>(body, h, HttpStatus.OK);
    }

    /* -------------------- UTIL -------------------- */
    private static Long extractUserId(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null)
            throw new UnauthorizedException("Token tidak valid / hilang");
        try {
            return Long.parseLong(auth.getPrincipal().toString());
        } catch (NumberFormatException ex) {
            throw new UnauthorizedException("Token tidak valid");
        }
    }

    private static boolean hasRole(Authentication auth, String role) {
        if (auth == null) return false;
        for (GrantedAuthority ga : auth.getAuthorities()) {
            if (role.equals(ga.getAuthority())) return true;
        }
        return false;
    }

    /* (masih dipakai di tempat lain) */
    @SuppressWarnings("unused")
    private static String sanitize(String s) {
        String raw = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^A-Za-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "_");
        return raw.toLowerCase();
    }
}
