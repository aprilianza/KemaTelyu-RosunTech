package com.kematelyu.kematelyu.dto;

import com.kematelyu.kematelyu.model.User;

public class LoginResponse {

    private String message;
    private User user;
    private String token;

    public LoginResponse(String message, User user, String token) {
        this.message = message;
        this.user = user;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
