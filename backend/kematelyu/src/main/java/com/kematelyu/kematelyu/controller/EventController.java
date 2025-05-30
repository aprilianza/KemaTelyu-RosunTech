package com.kematelyu.kematelyu.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kematelyu.kematelyu.dto.CreateEventRequest;
import com.kematelyu.kematelyu.dto.EventDetailDTO;
import com.kematelyu.kematelyu.dto.EventFullDTO;
import com.kematelyu.kematelyu.exception.ForbiddenException;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Registration;
import com.kematelyu.kematelyu.service.EventService;

@RestController
@RequestMapping("/api/events")
@CrossOrigin
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    /* ---------- helper ---------- */
    private ResponseEntity<Map<String, Object>> ok(Object data) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", 200);
        body.put("status", "OK");
        body.put("message", data);
        return ResponseEntity.ok(body);
    }

    /*
     * ======================================================================
     * CRUD EVENT – hanya STAFF
     */

    @PostMapping
    public ResponseEntity<Map<String, Object>> createEvent(@RequestBody CreateEventRequest dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();

        if (!"ROLE_STAFF".equals(role))
            throw new ForbiddenException("Hanya akun STAFF yang boleh membuat event");

        Event ev = service.createEvent(dto, userId);
        return ok(ev);
    }

    @GetMapping
    public List<EventFullDTO> getAll() {
        return service.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventFullDTO getOne(@PathVariable Long id) {
        return service.getEventByIdAsDTO(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
            @RequestBody CreateEventRequest dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();
        if (!"ROLE_STAFF".equals(role))
            throw new ForbiddenException("Hanya akun STAFF yang boleh mengedit event");

        Event old = service.byId(id);
        if (!old.getCreatedBy().getId().equals(userId))
            throw new ForbiddenException("Staff ini tidak berhak mengedit event yang dibuat oleh staff lain.");

        // Overwrite semua field
        old.setTitle(dto.getTitle());
        old.setDescription(dto.getDescription());
        old.setDate(dto.getDate());
        old.setTime(dto.getTime());
        old.setMaxParticipant(dto.getMaxParticipant());
        String foto = dto.getFotoPath();
        if (foto != null && !foto.startsWith("events/"))
            foto = "events/" + foto;
        old.setFotoPath(foto);

        Event updated = service.saveEvent(old);
        return ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> patch(@PathVariable Long id,
            @RequestBody CreateEventRequest dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();
        if (!"ROLE_STAFF".equals(role))
            throw new ForbiddenException("Hanya akun STAFF yang boleh mengedit event");

        Event old = service.byId(id);
        if (!old.getCreatedBy().getId().equals(userId))
            throw new ForbiddenException("Staff ini tidak berhak mengedit event yang dibuat oleh staff lain.");

        // Partial update: hanya yang non-null
        if (dto.getTitle() != null) old.setTitle(dto.getTitle());
        if (dto.getDescription() != null) old.setDescription(dto.getDescription());
        if (dto.getDate() != null) old.setDate(dto.getDate());
        if (dto.getTime() != null) old.setTime(dto.getTime());
        if (dto.getMaxParticipant() != null) old.setMaxParticipant(dto.getMaxParticipant());
        if (dto.getFotoPath() != null) {
            String foto = dto.getFotoPath();
            if (!foto.startsWith("events/")) foto = "events/" + foto;
            old.setFotoPath(foto);
        }

        Event updated = service.saveEvent(old);
        return ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();

        if (!"ROLE_STAFF".equals(role))
            throw new ForbiddenException("Hanya staff yang boleh menghapus event.");

        Event event = service.byId(id);
        if (!event.getCreatedBy().getId().equals(userId))
            throw new ForbiddenException("Staff ini tidak berhak menghapus event yang dibuat oleh staff lain.");

        service.delete(id);
        return ok("Event berhasil dihapus oleh pembuatnya.");
    }

    /*
     * ======================================================================
     * REGISTRASI MAHASISWA KE EVENT
     */

    @PostMapping("/{id}/register")
    public ResponseEntity<Map<String, Object>> registerToEvent(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Registration reg = service.registerToEventByUser(userId, id);
        return ok("Pendaftaran berhasil, status: " + reg.getStatus());
    }

    /*
     * ======================================================================
     * INFORMASI PESERTA
     */

    @GetMapping("/{id}/participants")
    public ResponseEntity<Map<String, Object>> getParticipants(@PathVariable Long id) {
        return ok(service.getParticipants(id));
    }

    @GetMapping("/{eventId}/participants/status")
    public ResponseEntity<Map<String, Object>> getParticipantsByStatus(
            @PathVariable Long eventId,
            @RequestParam String status) {
        return ok(service.getParticipantsByStatus(eventId, status));
    }

    /*
     * ======================================================================
     * CERTIFICATE
     */

    @PostMapping("/{id}/generate-certificate")
    public ResponseEntity<Map<String, Object>> generateCertificate(
            @PathVariable Long id,
            @RequestParam String nim) {
        return ok(service.generateCertificate(id, nim));
    }

    /*
     * ======================================================================
     * DETAIL & LIST MY EVENTS
     */

    @GetMapping("/{id}/detail")
    public EventDetailDTO getEventDetail(@PathVariable Long id) {
        return service.getEventDetailById(id);
    }

    @GetMapping("/staff/myevents")
    public ResponseEntity<?> getEventsByStaff() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String && principal.equals("anonymousUser")) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("code", 401);
            error.put("status", "Unauthorized");
            error.put("message", "Token tidak valid atau tidak ditemukan.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        Long userId = (Long) principal;
        return ok(service.getEventsByStaff(userId));
    }
}
