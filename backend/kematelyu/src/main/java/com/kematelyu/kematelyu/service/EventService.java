package com.kematelyu.kematelyu.service;

import com.kematelyu.kematelyu.exception.ResourceNotFoundException;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Mahasiswa;
import com.kematelyu.kematelyu.model.Registration;
import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.repository.CertificateRepository;
import com.kematelyu.kematelyu.repository.EventRepository;
import com.kematelyu.kematelyu.repository.MahasiswaRepository;
import com.kematelyu.kematelyu.repository.RegistrationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private MahasiswaRepository mahasiswaRepo;
    @Autowired
    private RegistrationRepository regRepo;
    @Autowired
    private CertificateRepository certificateRepo;

    private final EventRepository repo;

    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    public List<Event> all() {
        return repo.findAll();
    }

    public Event byId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event id " + id + " not found"));
    }

    public Event save(Event e) {
        return repo.save(e);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Registration registerToEvent(Long eventId, String nim) {
        Event event = byId(eventId);
        Mahasiswa m = mahasiswaRepo.findByNim(nim)
                .orElseThrow(() -> new ResourceNotFoundException("Mahasiswa tidak ditemukan"));

        // Cek jika sudah pernah daftar
        if (regRepo.existsByEventAndMahasiswa(event, m)) {
            throw new IllegalStateException("Sudah terdaftar di event ini.");
        }

        Registration reg = new Registration();
        reg.setEvent(event);
        reg.setMahasiswa(m);
        reg.setStatus(Registration.Status.PENDING);
        reg.setVerified(false);

        return regRepo.save(reg);
    }

    public List<Registration> getParticipants(Long eventId) {
        Event event = byId(eventId);
        return regRepo.findAllByEvent(event);
    }

    public Registration approveParticipant(Long registrationId) {
        Registration reg = regRepo.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registrasi tidak ditemukan"));

        reg.setStatus(Registration.Status.APPROVED);
        reg.setVerified(true);

        return regRepo.save(reg);
    }

    public Certificate generateCertificate(Long eventId, String nim) {
        Event event = byId(eventId);
        Mahasiswa m = mahasiswaRepo.findByNim(nim)
                .orElseThrow(() -> new ResourceNotFoundException("Mahasiswa tidak ditemukan"));

        Registration reg = regRepo.findByEventAndMahasiswa(event, m)
                .orElseThrow(() -> new ResourceNotFoundException("Belum daftar event"));

        if (!reg.isVerified()) {
            throw new IllegalStateException("Belum diverifikasi");
        }

        Certificate c = new Certificate();
        c.setEvent(event);
        c.setMahasiswa(m);
        c.setIssueDate(LocalDate.now());

        return certificateRepo.save(c);
    }
}
