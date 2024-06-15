package com.example.currencyconverter.errors;

import org.springframework.http.HttpStatus;

/**
 * Model for API errors
 * @param status
 *  HTTP status for the error
 * @param errors
 *  List of error messages
 *
 * @author Ivaylo Zahariev
 */
public record ApiError(HttpStatus status, int code, String... errors) {
    public ApiError(HttpStatus status, String... error) {
        this(status, status.value(), error);
    }
}
