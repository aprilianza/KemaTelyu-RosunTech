package com.kematelyu.kematelyu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.model.Mahasiswa;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByMahasiswa(Mahasiswa mahasiswa);
}
