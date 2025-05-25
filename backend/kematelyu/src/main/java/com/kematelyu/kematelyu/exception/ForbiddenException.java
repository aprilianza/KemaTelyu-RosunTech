package com.kematelyu.kematelyu.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message == null ? "Forbidden" : message);
    }
}
