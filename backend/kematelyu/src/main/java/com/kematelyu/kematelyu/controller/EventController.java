package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.CreateEventRequest;
import com.kematelyu.kematelyu.dto.EventDetailDTO;
import com.kematelyu.kematelyu.dto.EventSummaryDTO;
import com.kematelyu.kematelyu.exception.ForbiddenException;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Registration;
import com.kematelyu.kematelyu.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@CrossOrigin
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody CreateEventRequest dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();

        if (!"ROLE_STAFF".equals(role))
            throw new ForbiddenException("Hanya akun STAFF yang boleh membuat event");

        return ResponseEntity.ok(service.createEvent(dto, userId));
    }

    @GetMapping
    public List<EventSummaryDTO> getAll() {
        return service.getAllEvents();
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

        String foto = dto.getFotoPath();
        if (foto != null && !foto.startsWith("events/")) {
            foto = "events/" + foto;
        }
        old.setFotoPath(foto);

        return service.createEvent(dto, old.getCreatedBy().getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();

        if (!"ROLE_STAFF".equals(role))
            throw new ForbiddenException("Hanya staff yang boleh menghapus event.");

        Event event = service.byId(id);

        if (!event.getCreatedBy().getId().equals(userId))
            throw new ForbiddenException("Staff ini tidak berhak menghapus event yang dibuat oleh staff lain.");

        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Event berhasil dihapus oleh pembuatnya."));
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<String> registerToEvent(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Registration reg = service.registerToEventByUser(userId, id);
        return ResponseEntity.ok("Pendaftaran berhasil, status: " + reg.getStatus());
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<?> getParticipants(@PathVariable Long id) {
        return ResponseEntity.ok(service.getParticipants(id));
    }

    @GetMapping("/{eventId}/participants/status")
    public ResponseEntity<?> getParticipantsByStatus(
            @PathVariable Long eventId,
            @RequestParam String status) {
        return ResponseEntity.ok(service.getParticipantsByStatus(eventId, status));
    }

    @PutMapping("/participants/{registrationId}/approve")
    public ResponseEntity<String> approveParticipant(@PathVariable Long registrationId) {
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();
        if (!"ROLE_STAFF".equals(role))
            throw new ForbiddenException("Hanya staff yang boleh meng-approve pendaftaran.");

        Registration r = service.approveParticipant(registrationId);
        return ResponseEntity.ok("Berhasil approve peserta: " + r.getMahasiswa().getName());
    }

    @PutMapping("/participants/{registrationId}/reject")
    public ResponseEntity<String> rejectParticipant(@PathVariable Long registrationId) {
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();
        if (!"ROLE_STAFF".equals(role))
            throw new ForbiddenException("Hanya staff yang boleh menolak pendaftaran.");

        Registration r = service.rejectParticipant(registrationId);
        return ResponseEntity.ok("Pendaftaran ditolak untuk: " + r.getMahasiswa().getName());
    }

    @PostMapping("/{id}/generate-certificate")
    public ResponseEntity<?> generateCertificate(
            @PathVariable Long id,
            @RequestParam String nim) {
        return ResponseEntity.ok(service.generateCertificate(id, nim));
    }

    @GetMapping("/summary")
    public List<EventSummaryDTO> getEventSummaries() {
        return service.getAllEvents();
    }

    @GetMapping("/{id}/detail")
    public EventDetailDTO getEventDetail(@PathVariable Long id) {
        return service.getEventDetailById(id);
    }
}
