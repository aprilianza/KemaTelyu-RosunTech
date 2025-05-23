package com.kematelyu.kematelyu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    /* <<< PASSWORD TIDAK DI-SERIALISE KE JSON >>> */
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    /* ---------- FOTO ---------- */
    @Column(name = "foto_path")
    private String fotoPath; // e.g. "certificate/user_image/7.png"

    public User() {
    }

    public User(Long id, String name, String email,
            String password, String role, String fotoPath) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fotoPath = fotoPath;
    }

    /* ---------- EXCEPTIONS ---------- */
    public static class AkunTidakTerdaftarException extends Exception {
        public AkunTidakTerdaftarException(String msg) {
            super(msg);
        }
    }

    public static class PasswordSalahException extends Exception {
        public PasswordSalahException(String msg) {
            super(msg);
        }
    }

    /* ---------- GETTERS / SETTERS ---------- */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }
}
