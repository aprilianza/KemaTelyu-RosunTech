package com.kematelyu.kematelyu.repository;

import com.kematelyu.kematelyu.model.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Long> {
    Optional<Mahasiswa> findByNim(String nim);
}
