package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entity  : Event
 * Author  : Konogok (rev 2025-05-20)
 *
 * Tambahan:
 *   • fotoPath        – path file gambar poster/foto event (disimpan di DB sebagai text)
 *   • maxParticipant  – kuota maksimal peserta
 *   • certificates    – relasi 1-to-many ke Certificate
 */
@Entity
public class Event implements CertificateGenerator {

    /* ---------- primary ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- basic info ---------- */
    private String title;

    @Column(length = 8_000)
    private String description;

    private LocalDate date;

    /* ---------- NEW fields ---------- */
    private String fotoPath;          // contoh: /uploads/events/ai-ethics.jpg
    private Integer maxParticipant;   // kuota

    /* ---------- relations ---------- */
    @ManyToOne(fetch = FetchType.LAZY)
    private Staff createdBy;

    @OneToMany(mappedBy = "event",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Set<Registration> registrations = new HashSet<>();

    @OneToMany(mappedBy = "event",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Set<Certificate> certificates = new HashSet<>();

    /* ---------- ctor ---------- */
    public Event() {}

    public Event(Long id, String title, String description,
                 LocalDate date, String fotoPath,
                 Integer maxParticipant, Staff createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.fotoPath = fotoPath;
        this.maxParticipant = maxParticipant;
        this.createdBy = createdBy;
    }

    /* ---------- business logic ---------- */
    @Override
    public Certificate generateCertificate(Mahasiswa mhs) {
        return new Certificate(null, mhs, this, LocalDate.now());
    }

    public List<Mahasiswa> getParticipants() {
        return registrations.stream()
                            .map(Registration::getMahasiswa)
                            .collect(Collectors.toList());
    }

    /* ---------- getters & setters ---------- */
    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }

    public String getTitle()                  { return title; }
    public void setTitle(String title)        { this.title = title; }

    public String getDescription()            { return description; }
    public void setDescription(String d)      { this.description = d; }

    public LocalDate getDate()                { return date; }
    public void setDate(LocalDate date)       { this.date = date; }

    public String getFotoPath()               { return fotoPath; }
    public void setFotoPath(String fotoPath)  { this.fotoPath = fotoPath; }

    public Integer getMaxParticipant()        { return maxParticipant; }
    public void setMaxParticipant(Integer m)  { this.maxParticipant = m; }

    public Staff getCreatedBy()               { return createdBy; }
    public void setCreatedBy(Staff createdBy) { this.createdBy = createdBy; }

    public Set<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(Set<Registration> regs) { this.registrations = regs; }

    public Set<Certificate> getCertificates()   { return certificates; }
    public void setCertificates(Set<Certificate> certs) { this.certificates = certs; }
}
