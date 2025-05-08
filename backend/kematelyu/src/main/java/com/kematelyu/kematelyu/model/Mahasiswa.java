package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Mahasiswa extends User {
    private String nim;
    private String fakultas;
}
