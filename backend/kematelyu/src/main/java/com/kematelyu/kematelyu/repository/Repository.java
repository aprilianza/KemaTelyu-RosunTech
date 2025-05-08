package com.kematelyu.kematelyu.repository;

import com.kematelyu.kematelyu.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Long> {}
public interface StaffRepository extends JpaRepository<Staff, Long> {}
public interface EventRepository extends JpaRepository<Event, Long> {}
public interface RegistrationRepository extends JpaRepository<Registration, Long> {}
public interface CertificateRepository extends JpaRepository<Certificate, Long> {}
