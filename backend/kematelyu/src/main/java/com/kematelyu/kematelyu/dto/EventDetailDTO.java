package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;

/**
 * DTO lengkap untuk detail event.
 */
public class EventDetailDTO {

    /* ---------- FIELDS ---------- */
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String location; // (belum dipakai, biarkan null⇢future-proof)
    private Integer quota; // (belum dipakai, biarkan null⇢future-proof)
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
            String location, Integer quota,
            String fotoPath, Integer maxParticipant) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.quota = quota;
        this.fotoPath = fotoPath;
        this.maxParticipant = maxParticipant;
    }

    /**
     * Ctor ringkas (6 arg) — persis seperti yang dipanggil di EventService.
     * Location & quota diset null (atau bisa default sendiri nanti).
     */
    public EventDetailDTO(Long id, String title, String description, LocalDate date,
            String fotoPath, Integer maxParticipant) {
        this(id, title, description, date, null, null, fotoPath, maxParticipant);
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
