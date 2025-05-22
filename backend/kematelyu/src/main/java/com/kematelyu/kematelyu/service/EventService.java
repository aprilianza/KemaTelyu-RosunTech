package com.kematelyu.kematelyu.service;

import com.kematelyu.kematelyu.dto.CreateEventRequest;
import com.kematelyu.kematelyu.dto.EventDetailDTO;
import com.kematelyu.kematelyu.dto.EventSummaryDTO;
import com.kematelyu.kematelyu.exception.ResourceNotFoundException;
import com.kematelyu.kematelyu.model.*;
import com.kematelyu.kematelyu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository repo;
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private MahasiswaRepository mahasiswaRepo;
    @Autowired
    private RegistrationRepository regRepo;
    @Autowired
    private CertificateRepository certificateRepo;

    /* -------------------- DTO ENDPOINTS -------------------- */

    public List<EventSummaryDTO> getAllEvents() {
        return repo.findAll().stream()
                .map(event -> new EventSummaryDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDate(),
                        event.getFotoPath()))
                .collect(Collectors.toList());
    }

    public EventDetailDTO getEventDetailById(Long id) {
        Event event = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event id " + id + " not found"));

        return new EventDetailDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getFotoPath(),
                event.getMaxParticipant());
    }

    /* -------------------- CRUD -------------------- */

    public List<Event> all() {
        return repo.findAll();
    }

    public Event byId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event id " + id + " not found"));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    /* -------------------- CREATE -------------------- */

    public Event createEvent(CreateEventRequest dto, Long staffId) {
        Staff staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        Event e = new Event();
        e.setTitle(dto.getTitle());
        e.setDescription(dto.getDescription());
        e.setDate(dto.getDate());
        e.setTime(dto.getTime());

        String fotoPath = dto.getFotoPath();
        if (fotoPath != null && !fotoPath.startsWith("uploads/events/")) {
            fotoPath = "uploads/events/" + fotoPath;
        }
        e.setFotoPath(fotoPath);

        e.setMaxParticipant(dto.getMaxParticipant());
        e.setCreatedBy(staff);
        return repo.save(e);
    }

    /* -------------------- pendaftaran & sertifikat -------------------- */

    public Registration registerToEvent(Long eventId, String nim) {
        Event event = byId(eventId);
        Mahasiswa m = mahasiswaRepo.findByNim(nim)
                .orElseThrow(() -> new ResourceNotFoundException("Mahasiswa tidak ditemukan"));

        if (regRepo.existsByEventAndMahasiswa(event, m))
            throw new IllegalStateException("Sudah terdaftar di event ini.");

        Registration reg = new Registration(m, event);
        return regRepo.save(reg);
    }

    public Registration registerToEventByUser(Long userId, Long eventId) {
        User user = mahasiswaRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Mahasiswa tidak ditemukan"));

        if (!(user instanceof Mahasiswa mhs))
            throw new IllegalStateException("Hanya mahasiswa yang dapat mendaftar");

        Event event = byId(eventId);

        if (regRepo.existsByEventAndMahasiswa(event, mhs))
            throw new IllegalStateException("Sudah terdaftar di event ini.");

        if (event.getRegistrations().size() >= event.getMaxParticipant())
            throw new IllegalStateException("Kuota event sudah penuh.");

        Registration reg = new Registration(mhs, event);
        return regRepo.save(reg);
    }

    public List<Registration> getParticipants(Long eventId) {
        return regRepo.findAllByEvent(byId(eventId));
    }

    public List<Registration> getParticipantsByStatus(Long eventId, String status) {
        Event event = byId(eventId);
        return regRepo.findAllByEvent(event).stream()
                .filter(r -> r.getStatus().name().equalsIgnoreCase(status))
                .toList();
    }

    /** PUT /api/events/participants/{id}/approve */
    public Registration approveParticipant(Long registrationId) {
        Registration reg = regRepo.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registrasi tidak ditemukan"));

        reg.setStatus(Registration.Status.APPROVED);
        reg.setVerified(true);
        regRepo.save(reg);

        // 🔥 otomatis generate sertifikat saat di-approve
        Event event = reg.getEvent();
        Mahasiswa mhs = reg.getMahasiswa();

        if (!certificateRepo.existsByEventAndMahasiswa(event, mhs)) {
            Certificate cert = event.generateCertificate(mhs);
            certificateRepo.save(cert);
        }

        return reg;
    }

    public Registration rejectParticipant(Long registrationId) {
        Registration reg = regRepo.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registrasi tidak ditemukan"));
        reg.cancel();
        return regRepo.save(reg);
    }

    /**
     * Generate sertifikat manual (fallback).
     * Dipakai kalau butuh regenerate via service lain.
     */
    public Certificate generateCertificate(Long eventId, String nim) {
        Event event = byId(eventId);
        Mahasiswa m = mahasiswaRepo.findByNim(nim)
                .orElseThrow(() -> new ResourceNotFoundException("Mahasiswa tidak ditemukan"));

        if (certificateRepo.existsByEventAndMahasiswa(event, m))
            throw new IllegalStateException("Sertifikat sudah dibuat.");

        Registration reg = regRepo.findByEventAndMahasiswa(event, m)
                .orElseThrow(() -> new ResourceNotFoundException("Belum daftar event"));
        if (!reg.isVerified())
            throw new IllegalStateException("Belum diverifikasi");

        Certificate c = event.generateCertificate(m);
        return certificateRepo.save(c);
    }
}
