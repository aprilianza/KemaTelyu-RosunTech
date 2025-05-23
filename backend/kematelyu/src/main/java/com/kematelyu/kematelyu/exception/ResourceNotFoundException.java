package com.kematelyu.kematelyu.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message == null ? "Resource not found" : message);
    }
}
