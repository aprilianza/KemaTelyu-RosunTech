package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// asdasd
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin
public class EventController {

    private final EventService service;

    @GetMapping
    public List<Event> getAll(){ return service.all(); }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable Long id){ return service.byId(id); }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody Event e){
        return ResponseEntity.ok(service.save(e));
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @RequestBody Event e){
        Event old = service.byId(id);
        old.setTitle(e.getTitle());
        old.setDescription(e.getDescription());
        old.setDate(e.getDate());
        return service.save(old);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ service.delete(id); }
}
