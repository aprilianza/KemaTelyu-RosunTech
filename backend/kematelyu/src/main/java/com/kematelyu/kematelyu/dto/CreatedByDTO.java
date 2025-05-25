package com.kematelyu.kematelyu.dto;

public class CreatedByDTO {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String fotoPath;
    private String divisi;

    public CreatedByDTO() {
    }

    public CreatedByDTO(Long id,
            String name,
            String email,
            String role,
            String fotoPath,
            String divisi) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.fotoPath = fotoPath;
        this.divisi = divisi;
    }

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

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }
}
