package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Certificate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne private Mahasiswa mahasiswa;
    @ManyToOne private Event event;
    private LocalDate issueDate;

    public Certificate() {}

    /* helper-constructor dipakai Event.generateCertificate */
    public Certificate(Long id, Mahasiswa mahasiswa,
                       Event event, LocalDate issueDate) {
        this.id = id;
        this.mahasiswa = mahasiswa;
        this.event = event;
        this.issueDate = issueDate;
    }

    /* getters & setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Mahasiswa getMahasiswa() { return mahasiswa; }
    public void setMahasiswa(Mahasiswa mahasiswa) { this.mahasiswa = mahasiswa; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
}
