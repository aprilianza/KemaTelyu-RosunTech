package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;

public class CreateEventRequest {
    private String title;
    private String description;
    private LocalDate date;
    private Integer maxParticipant;
    private String fotoPath;   // path gambar yang sudah di-upload

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getMaxParticipant() { return maxParticipant; }
    public void setMaxParticipant(Integer maxParticipant) { this.maxParticipant = maxParticipant; }

    public String getFotoPath() { return fotoPath; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }
}
