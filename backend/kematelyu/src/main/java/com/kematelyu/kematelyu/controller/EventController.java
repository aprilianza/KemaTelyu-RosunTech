package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.dto.CreateEventRequest;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.service.EventService;
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
    public List<Event> getAll() {
        return service.all();
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
    public ResponseEntity<?> registerToEvent(@PathVariable Long id,
                                             @RequestParam String nim) {
        return ResponseEntity.ok(service.registerToEvent(id, nim));
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<?> getParticipants(@PathVariable Long id) {
        return ResponseEntity.ok(service.getParticipants(id));
    }

    @PutMapping("/participants/{registrationId}/approve")
    public ResponseEntity<?> approveParticipant(@PathVariable Long registrationId) {
        return ResponseEntity.ok(service.approveParticipant(registrationId));
    }

    /* --------------------- CERTIFICATE ------------------------ */
    @PostMapping("/{id}/generate-certificate")
    public ResponseEntity<?> generateCertificate(@PathVariable Long id,
                                                 @RequestParam String nim) {
        return ResponseEntity.ok(service.generateCertificate(id, nim));
    }
}
