package com.kematelyu.kematelyu.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message == null ? "Unauthorized" : message);
    }
}
