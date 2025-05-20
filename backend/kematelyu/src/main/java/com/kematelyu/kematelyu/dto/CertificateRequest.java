package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;

public class CertificateRequest {

    private Long mahasiswaId;
    private Long eventId;
    private LocalDate issueDate;

    public CertificateRequest() {}

    public CertificateRequest(Long mahasiswaId, Long eventId, LocalDate issueDate) {
        this.mahasiswaId = mahasiswaId;
        this.eventId = eventId;
        this.issueDate = issueDate;
    }

    public Long getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(Long mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
