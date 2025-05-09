package com.kematelyu.kematelyu.model;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Mahasiswa extends User {
    private String nim;
    private String fakultas;

    public List<Event> viewEvent(List<Event> allEvents) {
        return allEvents;
    }
    public Registration register(Event event) {
        return new Registration(this, event);
    }
    public String getNim() {
        return nim;
    }
    public void setNim(String nim) {
        if (nim == null || nim.trim().isEmpty()) {
            throw new IllegalArgumentException("NIM tidak boleh kosong.");
        }
        this.nim = nim;
    }
    public String getFakultas() {
        return fakultas;
    }
    public void setFakultas(String fakultas) {
        if (fakultas == null || fakultas.trim().isEmpty()) {
            throw new IllegalArgumentException("Fakultas tidak boleh kosong.");
        }
        this.fakultas = fakultas;
    }
}
