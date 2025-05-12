package com.kematelyu.kematelyu.dto;

import com.kematelyu.kematelyu.model.User;

public class LoginResponse {

    private String message;
    private User user;

    public LoginResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }

    /* getters */
    public String getMessage() { return message; }
    public User getUser() { return user; }
}
