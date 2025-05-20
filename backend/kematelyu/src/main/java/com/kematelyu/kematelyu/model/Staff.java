package com.kematelyu.kematelyu.model;
import java.util.List;

import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Staff extends User {

    private String divisi;

    public Staff() {}

    public Staff(String divisi) { this.divisi = divisi; }

    /* util */
    public void createEvent(Event event) {
        if (event != null) event.setCreatedBy(this);
    }
    public List<Mahasiswa> viewParticipant(Event event) {
        return event.getParticipants();
    }

    /* getters & setters */
    public String getDivisi() { return divisi; }
    public void setDivisi(String divisi) { this.divisi = divisi; }
}
