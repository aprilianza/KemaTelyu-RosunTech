package com.kematelyu.kematelyu.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Staff extends User {
    private String divisi;
}
