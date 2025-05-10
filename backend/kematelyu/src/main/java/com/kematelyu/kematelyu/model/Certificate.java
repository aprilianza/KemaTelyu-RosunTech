package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Certificate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne private Mahasiswa mahasiswa;
    @ManyToOne private Event event;
    private LocalDate issueDate;

    public void setId(int id) { 
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id tidak boleh kosong.");
        }

        if (usedIds.contains(id) && this.id != id) {
            throw new IllegalArgumentException("ID sudah digunakan! Harus unik.");
        }

        this.id = id;
        usedIds.add(id);
    }

    public void setMahasiswa(Mahasiswa mahasiswa) { 
        this.mahasiswa = mahasiswa; 
    }

    public void setEvent(Event event) { 
        this.event = event; 
    }

    public void setIssueDate(Date issueDate) { 
        this.issueDate = issueDate; 
    }

    public int getId() { 
        return id; 
    }

    public Mahasiswa getMahasiswa() { 
        return mahasiswa; 
    }

    public Event getEvent() { 
        return event; 
    }

    public Date getIssueDate() { 
        return issueDate; 
    }
}
