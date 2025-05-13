package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.service.EventService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<Event> getAll() {
        return service.all();
    }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable Long id) {
        return service.byId(id);
    }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody Event e) {
        return ResponseEntity.ok(service.save(e));
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @RequestBody Event e) {
        Event old = service.byId(id);
        old.setTitle(e.getTitle());
        old.setDescription(e.getDescription());
        old.setDate(e.getDate());
        return service.save(old);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<?> registerToEvent(
        @PathVariable Long id,
        @RequestParam String nim // atau ambil dari token
    ) {
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

    @PostMapping("/{id}/generate-certificate")
    public ResponseEntity<?> generateCertificate(
        @PathVariable Long id,
        @RequestParam String nim // atau ambil dari token
    ) {
        return ResponseEntity.ok(service.generateCertificate(id, nim));
    }
}
