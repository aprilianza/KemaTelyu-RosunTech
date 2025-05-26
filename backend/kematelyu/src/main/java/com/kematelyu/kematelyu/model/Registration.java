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

    public Registration() {}

    /* ctor praktis: new Registration(mhs, event) */
    public Registration(Mahasiswa mahasiswa, Event event) {
        this.mahasiswa = mahasiswa;
        this.event = event;
        this.date = LocalDate.now();
        this.status = Status.PENDING;
    }

    /* business */
    public void approve() {
        this.status = Status.APPROVED;
    }
    public void cancel() {
        this.status = Status.REJECTED;
    }
    public boolean isActive() {
        return status == Status.APPROVED;
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
}
