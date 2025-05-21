package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;

public class EventDetailDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String fotoPath;
    private Integer maxParticipant;

    public EventDetailDTO() {}

    public EventDetailDTO(Long id, String title, String description, LocalDate date, String fotoPath, Integer maxParticipant) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.fotoPath = fotoPath;
        this.maxParticipant = maxParticipant;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getFotoPath() { return fotoPath; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }

    public Integer getMaxParticipant() { return maxParticipant; }
    public void setMaxParticipant(Integer maxParticipant) { this.maxParticipant = maxParticipant; }
}
