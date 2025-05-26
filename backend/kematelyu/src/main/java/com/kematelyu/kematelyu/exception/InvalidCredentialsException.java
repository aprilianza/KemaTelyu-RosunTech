package com.kematelyu.kematelyu.exception;

public class InvalidCredentialsException extends UnauthorizedException {
    public InvalidCredentialsException() {
        super("Email / password salah");
    }
}
