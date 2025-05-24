package com.kematelyu.kematelyu.dto;

public class RegistrationDTO {

    private Long id;
    private String mahasiswaNim;
    private String status;
    private boolean verified;

    public RegistrationDTO() {
    }

    public RegistrationDTO(Long id,
            String mahasiswaNim,
            String status,
            boolean verified) {
        this.id = id;
        this.mahasiswaNim = mahasiswaNim;
        this.status = status;
        this.verified = verified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMahasiswaNim() {
        return mahasiswaNim;
    }

    public void setMahasiswaNim(String mahasiswaNim) {
        this.mahasiswaNim = mahasiswaNim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
