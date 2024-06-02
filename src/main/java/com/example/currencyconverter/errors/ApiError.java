package com.example.currencyconverter.errors;

import org.springframework.http.HttpStatus;

/**
 * Model for API errors
 * @param status
 *  HTTP status for the error
 * @param error
 *  Error message
 *
 * @author Ivaylo Zahariev
 */
public record ApiError(HttpStatus status, String error) {
}
