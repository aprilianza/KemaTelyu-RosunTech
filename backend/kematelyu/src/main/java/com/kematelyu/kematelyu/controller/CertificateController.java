package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.CertificateDTO;
import com.kematelyu.kematelyu.exception.ForbiddenException;
import com.kematelyu.kematelyu.exception.UnauthorizedException;
import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.service.CertificateService;
import com.kematelyu.kematelyu.util.CertificatePdfGenerator;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certService;

    public CertificateController(CertificateService certService) {
        this.certService = certService;
    }
    //ngasih komentar doang
    /* ---------- util: success wrapper ---------- */
    private ResponseEntity<Map<String, Object>> ok(Object data) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", 200);
        body.put("status", "OK");
        body.put("message", data);
        return ResponseEntity.ok(body);
    }

    /* ---------- LIST CERTIFICATE (JSON) ---------- */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getMyCertificates(Authentication auth) {

        if (auth == null || auth.getPrincipal() == null)
            throw new UnauthorizedException("Token missing");

        Long userId = (Long) auth.getPrincipal();
        List<Certificate> raw = certService.getCertificatesByMahasiswaId(userId);

        List<CertificateDTO> dto = raw.stream()
                .map(c -> new CertificateDTO(
                        c.getId(),
                        c.getEvent().getId(),
                        c.getEvent().getTitle(),
                        c.getEvent().getFotoPath(),
                        c.getIssueDate()))
                .toList();

        return ok(dto);
    }

    /* ---------- DOWNLOAD PDF ---------- */
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(
            @PathVariable Long id,
            Authentication auth) throws IOException {

        if (auth == null || auth.getPrincipal() == null)
            throw new UnauthorizedException("Token missing");

        Long userId = (Long) auth.getPrincipal();
        Certificate cert = certService.findById(id);

        if (!cert.getMahasiswa().getId().equals(userId))
            throw new ForbiddenException("Certificate does not belong to current user");

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
