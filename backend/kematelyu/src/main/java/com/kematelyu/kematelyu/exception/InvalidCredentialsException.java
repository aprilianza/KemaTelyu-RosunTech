package com.kematelyu.kematelyu.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Email / password salah");
    }
}