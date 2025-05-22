package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.CertificateDTO;
import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.service.CertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint:
 * GET /api/certificates -> List sertifikat user login
 */
@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certService;

    public CertificateController(CertificateService certService) {
        this.certService = certService;
    }

    @GetMapping
    public ResponseEntity<List<CertificateDTO>> getMyCertificates(Authentication auth) {

        Long userId = (Long) auth.getPrincipal(); // principal di-set JwtFilter
        List<Certificate> raw = certService.getCertificatesByMahasiswaId(userId);

        // 🔄 Map ke DTO → hindari serialisasi proxy Hibernate
        List<CertificateDTO> dto = raw.stream()
                .map(c -> new CertificateDTO(
                        c.getId(),
                        c.getEvent().getId(),
                        c.getEvent().getTitle(),
                        c.getEvent().getFotoPath(),
                        c.getIssueDate()))
                .toList();

        return ResponseEntity.ok(dto);
    }
}
