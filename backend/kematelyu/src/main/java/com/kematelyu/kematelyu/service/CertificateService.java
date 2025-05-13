package com.kematelyu.kematelyu.service;

import com.kematelyu.kematelyu.dto.CertificateRequest;
import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Mahasiswa;
import com.kematelyu.kematelyu.repository.CertificateRepository;
import com.kematelyu.kematelyu.repository.EventRepository;
import com.kematelyu.kematelyu.repository.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {

    @Autowired private CertificateRepository certificateRepository;
    @Autowired private MahasiswaRepository mahasiswaRepository;
    @Autowired private EventRepository eventRepository;

    public Certificate createCertificate(CertificateRequest request) {
        Mahasiswa mahasiswa = mahasiswaRepository.findById(request.getMahasiswaId())
                .orElseThrow(() -> new IllegalArgumentException("Mahasiswa tidak ditemukan"));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event tidak ditemukan"));

        Certificate certificate = new Certificate();
        certificate.setMahasiswa(mahasiswa);
        certificate.setEvent(event);
        certificate.setIssueDate(request.getIssueDate());

        return certificateRepository.save(certificate);
    }

    public List<Certificate> getCertificatesByMahasiswaId(Long mahasiswaId) {
        return certificateRepository.findByMahasiswa_Id(mahasiswaId);
    }
}
