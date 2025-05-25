package com.kematelyu.kematelyu.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

    private final int code;
    private final String status;

    public BaseException(HttpStatus httpStatus, String message) {
        super(message);
        this.code = httpStatus.value();
        this.status = httpStatus.getReasonPhrase();
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
