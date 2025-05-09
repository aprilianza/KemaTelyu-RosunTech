package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@MappedSuperclass
public abstract class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String name;
    @Column(nullable = false, unique = true) private String email;
    @Column(nullable = false) private String password;
    @Column(nullable = false) private String role;

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        }
        this.name = name;
    }
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Format email tidak valid.");
        }
        this.email = email;
    }
    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter.");
        }
        this.password = password;
    }
    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role tidak boleh kosong.");
        }
        this.role = role;
    }
    public boolean login(String inputEmail, String inputPassword)
            throws AkunTidakTerdaftarException, PasswordSalahException {
        if (!this.email.equals(inputEmail)) {
            throw new AkunTidakTerdaftarException("Akun tidak terdaftar");
        }
        if (!this.password.equals(inputPassword)) {
            throw new PasswordSalahException("Password salah");
        }
        return true;
    }
    public static class AkunTidakTerdaftarException extends Exception {
        public AkunTidakTerdaftarException(String message) {
            super(message);
        }
    }
    public static class PasswordSalahException extends Exception {
        public PasswordSalahException(String message) {
            super(message);
        }
    }
}