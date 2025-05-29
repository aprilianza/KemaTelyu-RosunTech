package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;

/**
 * DTO tampilan list pendaftaran seorang mahasiswa.
 * <p>
 * – registrationId : ID registrasi<br>
 * – eventId : ID event<br>
 * – eventName : judul event<br>
 * – eventDesc : deskripsi singkat event<br>
 * – registrationAt : tanggal mahasiswa mendaftar<br>
 * – status : PENDING | APPROVED | REJECTED<br>
 * – certificateId : ID sertifikat (null jika belum ada)
 */
public class RegistrationListDTO {

    private Long registrationId;
    private Long eventId;
    private String eventName;
    private String eventDesc;
    private LocalDate registrationAt;
    private String status;
    private Long certificateId; // bisa null

    public RegistrationListDTO() {
    }

    public RegistrationListDTO(Long registrationId,
            Long eventId,
            String eventName,
            String eventDesc,
            LocalDate registrationAt,
            String status,
            Long certificateId) {
        this.registrationId = registrationId;
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.registrationAt = registrationAt;
        this.status = status;
        this.certificateId = certificateId;
    }

    /* ---------- getters & setters ---------- */

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public LocalDate getRegistrationAt() {
        return registrationAt;
    }

    public void setRegistrationAt(LocalDate registrationAt) {
        this.registrationAt = registrationAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Long certificateId) {
        this.certificateId = certificateId;
    }
}
