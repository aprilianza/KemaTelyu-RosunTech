package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventSummaryDTO {

    private Long id;
    private String title;
    private LocalDate date;
    private LocalTime time;     // ✅ field baru
    private String fotoPath;

    public EventSummaryDTO() {}

    // ✅ Constructor lengkap dengan time
    public EventSummaryDTO(Long id, String title, LocalDate date, LocalTime time, String fotoPath) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.fotoPath = fotoPath;
    }

    /* ---------- GETTERS & SETTERS ---------- */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }
}
