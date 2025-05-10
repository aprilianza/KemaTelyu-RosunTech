package com.kematelyu.kematelyu.dto;

import com.kematelyu.kematelyu.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private User user;
}
