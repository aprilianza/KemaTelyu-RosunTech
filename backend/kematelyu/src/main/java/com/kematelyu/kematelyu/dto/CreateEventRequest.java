package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateEventRequest {
    private String title;
    private String description;
    private LocalDate date; // yyyy-MM-dd
    private LocalTime time; // HH:mm
    private Integer maxParticipant;
    private String fotoPath; // kirim cuma nama file (ex: ai.jpg)

    /* --- getters & setters --- */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getMaxParticipant() {
        return maxParticipant;
    }

    public void setMaxParticipant(Integer maxParticipant) {
        this.maxParticipant = maxParticipant;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }
}
