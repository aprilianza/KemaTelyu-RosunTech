package com.kematelyu.kematelyu.service;

import com.kematelyu.kematelyu.dto.*;
import com.kematelyu.kematelyu.exception.ResourceNotFoundException;
import com.kematelyu.kematelyu.model.*;
import com.kematelyu.kematelyu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public List<EventFullDTO> getAllEvents() {
        return repo.findAll().stream().map(this::mapToFullDTO).toList();
    }

    public EventDetailDTO getEventDetailById(Long id) {
        Event e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event id " + id + " not found"));
        return new EventDetailDTO(
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getDate(),
                e.getTime(),
                e.getFotoPath(),
                e.getMaxParticipant());
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

        ensureStaticEventsDir();

        Event e = new Event();
        e.setTitle(dto.getTitle());
        e.setDescription(dto.getDescription());
        e.setDate(dto.getDate());
        e.setTime(dto.getTime());

        String fotoPath = dto.getFotoPath();
        if (fotoPath != null && !fotoPath.startsWith("events/")) {
            fotoPath = "events/" + fotoPath;
        }
        e.setFotoPath(fotoPath);

        e.setMaxParticipant(dto.getMaxParticipant());
        e.setCreatedBy(staff);

        return repo.save(e);
    }

    /* -------------------- REGISTRASI -------------------- */

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

        return regRepo.save(new Registration(mhs, event));
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

    public Certificate generateCertificate(Long eventId, String nim) {
        Event event = byId(eventId);
        Mahasiswa m = mahasiswaRepo.findByNim(nim)
                .orElseThrow(() -> new ResourceNotFoundException("Mahasiswa tidak ditemukan"));

        if (certificateRepo.existsByEventAndMahasiswa(event, m))
            throw new IllegalStateException("Sertifikat sudah dibuat.");

        Registration reg = regRepo.findByEventAndMahasiswa(event, m)
                .orElseThrow(() -> new ResourceNotFoundException("Belum daftar event"));
        if (!reg.isActive())
            throw new IllegalStateException("Belum diverifikasi");

        return certificateRepo.save(event.generateCertificate(m));
    }

    /* -------------------- STAFF FILTER -------------------- */

    public List<EventSummaryDTO> getEventsByStaff(Long userId) {
        Staff staff = staffRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Staff tidak ditemukan"));
    
        return repo.findByCreatedBy(staff).stream()
            .map(event -> new EventSummaryDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getTime(),           
                event.getFotoPath()
            ))
            .collect(Collectors.toList());
    }

    /* -------------------- INTERNAL MAPPER -------------------- */

    private EventFullDTO mapToFullDTO(Event e) {

        CreatedByDTO createdByDTO = null;
        if (e.getCreatedBy() != null) {
            Staff s = e.getCreatedBy();
            createdByDTO = new CreatedByDTO(
                    s.getId(),
                    s.getName(),
                    s.getEmail(),
                    s.getRole(),
                    s.getFotoPath(),
                    s.getDivisi());
        }

        List<RegistrationDTO> regDTOs = e.getRegistrations().stream()
                .map(r -> new RegistrationDTO(
                        r.getId(),
                        r.getMahasiswa().getNim(),
                        r.getStatus().name()
                ))
                .toList();

        List<CertificateDTO> certDTOs = e.getCertificates().stream()
                .map(c -> new CertificateDTO(
                        c.getId(),
                        c.getMahasiswa().getNim(),
                        c.getIssueDate()))
                .toList();

        List<ParticipantDTO> participantDTOs = e.getParticipants().stream()
                .map(m -> new ParticipantDTO(
                        m.getId(),
                        m.getNim(),
                        m.getName(),
                        m.getEmail(),
                        m.getFakultas(),
                        m.getFotoPath()))
                .toList();

        return new EventFullDTO(
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getDate(),
                e.getTime(),
                e.getFotoPath(),
                e.getMaxParticipant(),
                createdByDTO,
                regDTOs,
                certDTOs,
                participantDTOs);
    }

    /* -------------------- UTILITY -------------------- */

    private void ensureStaticEventsDir() {
        try {
            Path p = Paths.get("src/main/resources/static/events");
            if (Files.notExists(p))
                Files.createDirectories(p);
        } catch (Exception ignored) {
        }
    }

    public Registration approveParticipant(Long registrationId) {
        Registration reg = regRepo.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registrasi tidak ditemukan"));

        reg.setStatus(Registration.Status.APPROVED);
        return regRepo.save(reg);
    }

    public Registration rejectParticipant(Long registrationId) {
        Registration reg = regRepo.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registrasi tidak ditemukan"));

        reg.setStatus(Registration.Status.REJECTED);
        return regRepo.save(reg);
    }
    
    public EventFullDTO getEventByIdAsDTO(Long id) {
        Event event = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        return mapToFullDTO(event); // sudah ada di class ini
    }
    
}
