package com.kematelyu.kematelyu.repository;

import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByMahasiswa(Mahasiswa mahasiswa);

    /** Cek supaya nggak bikin sertifikat dobel */
    boolean existsByEventAndMahasiswa(Event event, Mahasiswa mahasiswa);
}
