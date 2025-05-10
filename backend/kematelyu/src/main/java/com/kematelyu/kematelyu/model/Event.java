package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event implements CertificateGenerator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 8000)
    private String description;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Staff createdBy;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Registration> registrations = new HashSet<>();

    @Override
    public Certificate generateCertificate(Mahasiswa mhs) {
        Certificate cert = new Certificate(null, mhs, this, LocalDate.now());
        return cert;
    }

    public List<Mahasiswa> getParticipants() {
        return registrations.stream()
                .map(Registration::getMahasiswa)
                .collect(Collectors.toList());
    }
}
