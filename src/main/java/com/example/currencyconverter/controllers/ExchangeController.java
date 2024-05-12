package com.example.currencyconverter.controllers;

import com.example.currencyconverter.errors.ApiError;
import com.example.currencyconverter.services.ExchangeService;
import com.example.currencyconverter.util.enums.Currency;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping
    @RequestMapping("/get-rate")
    public double getRate(@RequestParam Currency from, @RequestParam Currency to) {
        return exchangeService.getRate(from, to, 1);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Class<?> requiredType = ex.getRequiredType();

        var apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Invalid parameter"
        );
        if (requiredType.isAssignableFrom(Currency.class)) {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, String.format("Unsupported currency '%s'", ex.getValue()));
        }

        return new ResponseEntity(apiError, new HttpHeaders(), apiError.status());
    }
}