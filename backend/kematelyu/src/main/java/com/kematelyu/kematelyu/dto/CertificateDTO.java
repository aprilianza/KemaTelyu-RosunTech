package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;

public class CertificateDTO {

    private Long id;
    private String mahasiswaNim;
    private Long eventId;
    private String eventTitle;
    private String fotoPath;
    private LocalDate issueDate;

    public CertificateDTO() {
    }

    public CertificateDTO(Long id, String mahasiswaNim, LocalDate issueDate) {
        this.id = id;
        this.mahasiswaNim = mahasiswaNim;
        this.issueDate = issueDate;
    }

    public CertificateDTO(Long id,
            String mahasiswaNim,
            Long eventId,
            String eventTitle,
            String fotoPath,
            LocalDate issueDate) {
        this.id = id;
        this.mahasiswaNim = mahasiswaNim;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.fotoPath = fotoPath;
        this.issueDate = issueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMahasiswaNim() {
        return mahasiswaNim;
    }

    public void setMahasiswaNim(String mahasiswaNim) {
        this.mahasiswaNim = mahasiswaNim;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
