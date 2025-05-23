package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO lengkap untuk detail event.
 */
public class EventDetailDTO {

    /* ---------- FIELDS ---------- */
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String location; // (belum dipakai, biarkan null⇢future-proof)
    private Integer quota;   // (belum dipakai, biarkan null⇢future-proof)
    private String fotoPath;
    private Integer maxParticipant;

    /* ---------- CTOR ---------- */

    /** No-args constructor (wajib untuk serialisasi). */
    public EventDetailDTO() {
    }

    /**
     * Ctor lengkap (8 arg). Pakai ini kalau sudah punya location & quota.
     */
    public EventDetailDTO(Long id, String title, String description, LocalDate date,
                          LocalTime time, String location, Integer quota,
                          String fotoPath, Integer maxParticipant) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.quota = quota;
        this.fotoPath = fotoPath;
        this.maxParticipant = maxParticipant;
    }

    /**
     * Ctor ringkas (7 arg) — dipakai oleh EventService.
     */
    public EventDetailDTO(Long id, String title, String description, LocalDate date,
                          LocalTime time, String fotoPath, Integer maxParticipant) {
        this(id, title, description, date, time, null, null, fotoPath, maxParticipant);
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public Integer getMaxParticipant() {
        return maxParticipant;
    }

    public void setMaxParticipant(Integer maxParticipant) {
        this.maxParticipant = maxParticipant;
    }
}
