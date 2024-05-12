package com.example.currencyconverter.errors;

import org.springframework.http.HttpStatus;

public record ApiError(HttpStatus status, String error) {
}
