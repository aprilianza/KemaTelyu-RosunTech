package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventFullDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String fotoPath;
    private Integer maxParticipant;
    private CreatedByDTO createdBy;
    private List<RegistrationDTO> registrations;
    private List<CertificateDTO> certificates;
    private List<ParticipantDTO> participants;

    public EventFullDTO() {
    }

    public EventFullDTO(Long id,
            String title,
            String description,
            LocalDate date,
            LocalTime time,
            String fotoPath,
            Integer maxParticipant,
            CreatedByDTO createdBy,
            List<RegistrationDTO> registrations,
            List<CertificateDTO> certificates,
            List<ParticipantDTO> participants) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.fotoPath = fotoPath;
        this.maxParticipant = maxParticipant;
        this.createdBy = createdBy;
        this.registrations = registrations;
        this.certificates = certificates;
        this.participants = participants;
    }

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

    public CreatedByDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedByDTO createdBy) {
        this.createdBy = createdBy;
    }

    public List<RegistrationDTO> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<RegistrationDTO> registrations) {
        this.registrations = registrations;
    }

    public List<CertificateDTO> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateDTO> certificates) {
        this.certificates = certificates;
    }

    public List<ParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDTO> participants) {
        this.participants = participants;
    }
}
