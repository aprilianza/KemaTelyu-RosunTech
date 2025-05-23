package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;

/**
 * Ringkasan sertifikat yang aman dikirim ke FE
 * (tanpa objek Hibernate Lazy).
 */
public class CertificateDTO {

    private Long id;
    private Long eventId;
    private String eventTitle;
    private String fotoPath;
    private LocalDate issueDate;

    public CertificateDTO() {
    }

    public CertificateDTO(Long id, Long eventId,
            String eventTitle, String fotoPath,
            LocalDate issueDate) {
        this.id = id;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.fotoPath = fotoPath;
        this.issueDate = issueDate;
    }

    /* ---------- GETTERS & SETTERS ---------- */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long id) {
        this.eventId = id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String t) {
        this.eventTitle = t;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String p) {
        this.fotoPath = p;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate d) {
        this.issueDate = d;
    }
}
