package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;

public class EventSummaryDTO {
    private Long id;
    private String title;
    private LocalDate date;
    private String fotoPath;

    public EventSummaryDTO() {}

    public EventSummaryDTO(Long id, String title, LocalDate date, String fotoPath) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.fotoPath = fotoPath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getFotoPath() { return fotoPath; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }
}
