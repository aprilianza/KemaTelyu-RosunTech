package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Registration {

    public enum Status { PENDING, APPROVED, REJECTED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne private Event event;
    @ManyToOne private Mahasiswa mahasiswa;

    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    public Registration() {}

    /* ctor praktis: new Registration(mhs, event) */
    public Registration(Mahasiswa mahasiswa, Event event) {
        this.mahasiswa = mahasiswa;
        this.event = event;
        this.date = LocalDate.now();
        this.status = Status.PENDING;
        this.verified = false;
    }

    /* business */
    public void approve() {
        this.status = Status.APPROVED;
        this.verified = true;
    }
    public void cancel() {
        this.status = Status.REJECTED;
        this.verified = false;
    }
    public boolean isActive() {
        return status == Status.APPROVED && verified;
    }

    /* getters & setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Mahasiswa getMahasiswa() { return mahasiswa; }
    public void setMahasiswa(Mahasiswa mahasiswa) { this.mahasiswa = mahasiswa; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
}
