package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.CertificateDTO;
import com.kematelyu.kematelyu.exception.ForbiddenException;
import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.service.CertificateService;
import com.kematelyu.kematelyu.util.CertificatePdfGenerator;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certService;

    public CertificateController(CertificateService certService) {
        this.certService = certService;
    }

    @GetMapping
    public ResponseEntity<List<CertificateDTO>> getMyCertificates(Authentication auth) {

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

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(
            @PathVariable Long id,
            Authentication auth) throws IOException {

        Long userId = (Long) auth.getPrincipal();
        Certificate cert = certService.findById(id);

        if (!cert.getMahasiswa().getId().equals(userId)) {
            throw new ForbiddenException("Certificate does not belong to current user");
        }

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
