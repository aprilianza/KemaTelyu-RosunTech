package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Event implements CertificateGenerator {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 8000)
    private String description;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Staff createdBy;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Registration> registrations = new HashSet<>();

    public Event() {}

    public Event(Long id, String title, String description,
                 LocalDate date, Staff createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.createdBy = createdBy;
    }

    /* ---------- logic ---------- */
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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Staff getCreatedBy() { return createdBy; }
    public void setCreatedBy(Staff createdBy) { this.createdBy = createdBy; }

    public Set<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(Set<Registration> registrations) {
        this.registrations = registrations;
    }
}
