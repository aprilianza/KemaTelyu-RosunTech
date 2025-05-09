package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Mahasiswa mahasiswa;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean isVerified;

    // ✅ Constructor tambahan biar bisa new Registration(mahasiswa, event)
    public Registration(Mahasiswa mahasiswa, Event event) {
        this.mahasiswa = mahasiswa;
        this.event = event;
        this.date = LocalDate.now();
        this.status = Status.PENDING;
        this.isVerified = false;
    }

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    public void approve() {
        this.status = Status.APPROVED;
        this.isVerified = true;
    }

    public void cancel() {
        this.status = Status.REJECTED;
        this.isVerified = false;
    }

    public boolean isActive() {
        return this.status == Status.APPROVED && this.isVerified;
    }

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status.name();
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status.toUpperCase());
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }
}
