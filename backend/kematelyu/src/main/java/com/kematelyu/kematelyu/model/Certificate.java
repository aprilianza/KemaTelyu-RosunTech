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
}
