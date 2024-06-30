package com.example.currencyconverter.models;

import org.springframework.http.HttpStatus;

/**
 * API response model. Contains HTTP status info, response content and errors
 * @param status
 *  HTTP status
 * @param code
 *  HTTP status code
 * @param content
 *  Contains the data returned by the API. Null if there are errors
 * @param errors
 *  A list of errors. Empty if no errors occurred
 * @param <T>
 *  The type of the content
 *
 * @author Ivaylo Zahariev
 */
public record ApiResponse<T>(HttpStatus status, int code, T content, String... errors) {
    public ApiResponse(HttpStatus status, T content, String... errors) {
        this(status, status.value(), content, errors);
    }
}