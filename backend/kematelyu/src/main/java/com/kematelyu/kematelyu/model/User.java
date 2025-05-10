package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String name;
    @Column(nullable = false, unique = true) private String email;
    @Column(nullable = false) private String password;
    @Column(nullable = false) private String role;

    public User() {}

    public User(Long id, String name, String email,
                String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /* BASIC LOGIN VALIDATION */
    public boolean login(String inputEmail, String inputPassword)
            throws AkunTidakTerdaftarException, PasswordSalahException {
        if (!this.email.equals(inputEmail))
            throw new AkunTidakTerdaftarException("Akun tidak terdaftar");

        if (!this.password.equals(inputPassword))
            throw new PasswordSalahException("Password salah");

        return true;
    }

    /* custom exceptions */
    public static class AkunTidakTerdaftarException extends Exception {
        public AkunTidakTerdaftarException(String msg) { super(msg); }
    }
    public static class PasswordSalahException extends Exception {
        public PasswordSalahException(String msg) { super(msg); }
    }

    /* getters & setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
