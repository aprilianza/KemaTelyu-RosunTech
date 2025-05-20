package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;

public class CertificateResponse {

    private Long certificateId;
    private String mahasiswaName;
    private String eventName;
    private LocalDate issueDate;

    public CertificateResponse() {}

    public CertificateResponse(Long certificateId, String mahasiswaName, String eventName, LocalDate issueDate) {
        this.certificateId = certificateId;
        this.mahasiswaName = mahasiswaName;
        this.eventName = eventName;
        this.issueDate = issueDate;
    }

    public Long getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Long certificateId) {
        this.certificateId = certificateId;
    }

    public String getMahasiswaName() {
        return mahasiswaName;
    }

    public void setMahasiswaName(String mahasiswaName) {
        this.mahasiswaName = mahasiswaName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
