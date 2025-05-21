package com.kematelyu.kematelyu.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kematelyu.kematelyu.dto.CertificateRequest;
import com.kematelyu.kematelyu.dto.CertificateResponse;
import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.service.CertificateService;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping
    public CertificateResponse createCertificate(@RequestBody CertificateRequest request) {
        Certificate cert = certificateService.createCertificate(request);
        return new CertificateResponse(
                cert.getId(),
                cert.getMahasiswa().getName(),
                cert.getEvent().getTitle(),
                cert.getIssueDate()
        );
    }

    @GetMapping("/mahasiswa/{mahasiswaId}")
    public List<CertificateResponse> getCertificatesByMahasiswa(@PathVariable Long mahasiswaId) {
        List<Certificate> certs = certificateService.getCertificatesByMahasiswaId(mahasiswaId);
        return certs.stream()
                .map(cert -> new CertificateResponse(
                        cert.getId(),
                        cert.getMahasiswa().getName(),
                        cert.getEvent().getTitle(),
                        cert.getIssueDate()))
                .collect(Collectors.toList());
    }
    
}
