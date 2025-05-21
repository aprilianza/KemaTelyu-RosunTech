package com.kematelyu.kematelyu.dto;

import java.time.LocalDate;

public class EventDetailDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String location;
    private Integer quota;

    public EventDetailDTO() {
        // No-args constructor
    }

    public EventDetailDTO(Long id, String title, String description, LocalDate date, String location, Integer quota) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.quota = quota;
    }

    // Getter & Setter semua field

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Tambahkan getter-setter lainnya sesuai kebutuhan
}
