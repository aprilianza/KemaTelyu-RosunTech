package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.CertificateDTO;
import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.service.CertificateService;
import com.kematelyu.kematelyu.util.CertificatePdfGenerator;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * GET /api/certificates -> list sertifikat user login
 * GET /api/certificates/{id}/download -> download PDF (owner only)
 */
@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certService;

    public CertificateController(CertificateService certService) {
        this.certService = certService;
    }

    /* ---------- LIST ---------- */
    @GetMapping
    public ResponseEntity<List<CertificateDTO>> getMyCertificates(Authentication auth) {

        Long userId = (Long) auth.getPrincipal(); // principal di-set JwtFilter
        List<Certificate> raw = certService.getCertificatesByMahasiswaId(userId);

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

    /* ---------- DOWNLOAD PDF ---------- */
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(
            @PathVariable Long id,
            Authentication auth) throws IOException {

        Long userId = (Long) auth.getPrincipal();
        Certificate cert = certService.findById(id);

        // ✅ hanya pemilik yg boleh download
        if (!cert.getMahasiswa().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(("Forbidden – certificate does not belong to current user").getBytes());
        }

        // generate PDF bytes
        byte[] pdf = CertificatePdfGenerator.generate(cert);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("sertifikat-" + cert.getId() + ".pdf")
                        .build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
