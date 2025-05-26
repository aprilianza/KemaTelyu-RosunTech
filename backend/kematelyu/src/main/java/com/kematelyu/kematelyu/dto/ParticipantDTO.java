package com.kematelyu.kematelyu.dto;

public class ParticipantDTO {

    private Long id;
    private String nim;
    private String name;
    private String email;
    private String fakultas;
    private String fotoPath;

    public ParticipantDTO() {
    }

    public ParticipantDTO(Long id,
            String nim,
            String name,
            String email,
            String fakultas,
            String fotoPath) {
        this.id = id;
        this.nim = nim;
        this.name = name;
        this.email = email;
        this.fakultas = fakultas;
        this.fotoPath = fotoPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
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

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }
}
