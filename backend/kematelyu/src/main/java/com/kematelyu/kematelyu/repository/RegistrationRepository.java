package com.kematelyu.kematelyu.repository;

import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Mahasiswa;
import com.kematelyu.kematelyu.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findAllByEvent(Event event);

    boolean existsByEventAndMahasiswa(Event event, Mahasiswa mahasiswa);

    Optional<Registration> findByEventAndMahasiswa(Event event, Mahasiswa mahasiswa);
}
