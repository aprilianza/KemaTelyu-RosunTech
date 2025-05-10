package com.kematelyu.kematelyu.model;

import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Mahasiswa extends User {

    private String nim;
    private String fakultas;

    public Mahasiswa() {}

    public Mahasiswa(String nim, String fakultas) {
        this.nim = nim;
        this.fakultas = fakultas;
    }

    /* util */
    public List<Event> viewEvent(List<Event> allEvents) {
        return allEvents;
    }

    public Registration register(Event event) {
        return new Registration(this, event);
    }

    /* getters & setters */
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getFakultas() { return fakultas; }
    public void setFakultas(String fakultas) { this.fakultas = fakultas; }
}
