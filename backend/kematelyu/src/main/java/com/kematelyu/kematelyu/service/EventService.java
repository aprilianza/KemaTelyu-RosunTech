package com.kematelyu.kematelyu.service;

import com.kematelyu.kematelyu.exception.ResourceNotFoundException;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repo;

    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    public List<Event> all() { return repo.findAll(); }

    public Event byId(Long id) {
        return repo.findById(id)
                   .orElseThrow(() ->
                       new ResourceNotFoundException("Event id " + id + " not found"));
    }

    public Event save(Event e) { return repo.save(e); }

    public void delete(Long id) { repo.deleteById(id); }
}
