package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Registration {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne private Event event;
    @ManyToOne private Mahasiswa mahasiswa;

    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean isVerified;

    public enum Status { PENDING, APPROVED, REJECTED }
}
