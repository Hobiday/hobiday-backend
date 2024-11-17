package com.example.culture.global.domain;

import org.springframework.http.HttpStatus;

public interface BaseError {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
