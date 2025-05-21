package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.CreateEventRequest;
import com.kematelyu.kematelyu.dto.EventDetailDTO;
import com.kematelyu.kematelyu.dto.EventSummaryDTO;
import com.kematelyu.kematelyu.exception.ResourceNotFoundException;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Mahasiswa;
import com.kematelyu.kematelyu.service.EventService;
import com.kematelyu.kematelyu.model.Registration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    /* --------- CREATE (STAFF ONLY) --------- */
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody CreateEventRequest dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority(); // ROLE_STAFF

        if (!"ROLE_STAFF".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Hanya akun STAFF yang boleh membuat event");
        }
        return ResponseEntity.ok(service.createEvent(dto, userId));
    }

    /* -------- BASIC CRUD -------- */
    @GetMapping
    public List<EventSummaryDTO> getAll() {
        return service.getAllEvents(); // ✅ return DTO untuk hindari lazy proxy error
    }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable Long id) {
        return service.byId(id);
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @RequestBody CreateEventRequest dto) {
        Event old = service.byId(id);
        old.setTitle(dto.getTitle());
        old.setDescription(dto.getDescription());
        old.setDate(dto.getDate());
        old.setTime(dto.getTime());
        old.setMaxParticipant(dto.getMaxParticipant());

        String fotoPath = dto.getFotoPath();
        if (fotoPath != null && !fotoPath.startsWith("uploads/events/")) {
            fotoPath = "uploads/events/" + fotoPath;
        }
        old.setFotoPath(fotoPath);

        return service.createEvent(dto, old.getCreatedBy().getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    /* ---------------------- REGISTRATION ---------------------- */
    @PostMapping("/{id}/register")
    public ResponseEntity<?> registerToEvent(@PathVariable Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Principal: " + auth.getPrincipal());
        System.out.println("Authorities: " + auth.getAuthorities());
        
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            Registration reg = service.registerToEventByUser(userId, id);
            return ResponseEntity.ok("Pendaftaran berhasil, status: " + reg.getStatus());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<?> getParticipants(@PathVariable Long id) {
        return ResponseEntity.ok(service.getParticipants(id));
    }

    @GetMapping("/{eventId}/participants/status")
    public ResponseEntity<?> getParticipantsByStatus(@PathVariable Long eventId,
                                                    @RequestParam String status) {
        return ResponseEntity.ok(service.getParticipantsByStatus(eventId, status));
    }

    @PutMapping("/participants/{registrationId}/approve")
    public ResponseEntity<?> approveParticipant(@PathVariable Long registrationId) {
        String role = SecurityContextHolder.getContext().getAuthentication()
            .getAuthorities().iterator().next().getAuthority();
        if (!"ROLE_STAFF".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Hanya staff yang boleh meng-approve pendaftaran.");
        }

        try {
            Registration approved = service.approveParticipant(registrationId);
            return ResponseEntity.ok("Berhasil approve peserta: " + approved.getMahasiswa().getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* --------------------- CERTIFICATE ------------------------ */
    @PostMapping("/{id}/generate-certificate")
    public ResponseEntity<?> generateCertificate(@PathVariable Long id,
                                                 @RequestParam String nim) {
        return ResponseEntity.ok(service.generateCertificate(id, nim));
    }

    /* --------------------- DTO ENDPOINTS ------------------------ */

    // Untuk homepage: daftar event ringkas
    @GetMapping("/summary")
    public List<EventSummaryDTO> getEventSummaries() {
        return service.getAllEvents();
    }

    // Untuk detail: detail lengkap 1 event
    @GetMapping("/{id}/detail")
    public EventDetailDTO getEventDetail(@PathVariable Long id) {
        return service.getEventDetailById(id);
    }
}
