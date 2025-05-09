package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Staff extends User {
    private String divisi;
    public void createEvent(Event event) {
        if (event != null) {
            event.setCreatedBy(this);
        }
    }
    public List<Mahasiswa> viewParticipant(Event event) {
        return event.getParticipants();
    }
    public String getDivisi() {
        return divisi;
    }
    public void setDivisi(String divisi) {
        if (divisi == null || divisi.trim().isEmpty()) {
            throw new IllegalArgumentException("Divisi tidak boleh kosong.");
        }
        this.divisi = divisi;
    }
}
