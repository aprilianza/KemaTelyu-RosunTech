package com.kematelyu.kematelyu.service;

import com.kematelyu.kematelyu.exception.ResourceNotFoundException;
import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.model.Registration;
import com.kematelyu.kematelyu.repository.CertificateRepository;
import com.kematelyu.kematelyu.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service khusus operasi pada entity Registration:
 * – approve / reject (oleh staff)
 * – logika sertifikat otomatis ketika approve
 */
@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository regRepo;
    @Autowired
    private CertificateRepository certRepo;

    /* ---------------------------------------------------------------------- */
    /* STAFF APPROVE */
    /* ---------------------------------------------------------------------- */
    public Registration approveParticipant(Long registrationId) {

        Registration reg = regRepo.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registrasi tidak ditemukan"));

        if (reg.getStatus() != Registration.Status.APPROVED) {
            reg.setStatus(Registration.Status.APPROVED);
            reg = regRepo.save(reg);

            // Buat sertifikat jika belum ada
            boolean issued = certRepo.existsByEventAndMahasiswa(reg.getEvent(), reg.getMahasiswa());
            if (!issued) {
                Certificate cert = reg.getEvent().generateCertificate(reg.getMahasiswa());
                certRepo.save(cert);
            }
        }
        return reg;
    }

    /* ---------------------------------------------------------------------- */
    /* STAFF REJECT */
    /* ---------------------------------------------------------------------- */
    public Registration rejectParticipant(Long registrationId) {

        Registration reg = regRepo.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registrasi tidak ditemukan"));

        reg.setStatus(Registration.Status.REJECTED);
        return regRepo.save(reg);
    }
}
