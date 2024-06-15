package com.example.currencyconverter.controllers;

import com.example.currencyconverter.errors.ApiError;
import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.models.CurrencyConversionData;
import com.example.currencyconverter.services.ExchangeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for exchange APIs
 *
 * @author Ivaylo Zahariev
 */
@RestController
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    /**
     * Returns the exchange rate for the given currencies
     * @param from
     *  The source currency
     * @param to
     *  The convert currency
     */
    @GetMapping
    @RequestMapping("/get-rate")
    public double getRate(@RequestParam Currency from, @RequestParam Currency to) {
        return exchangeService.getRate(from, to, 1);
    }

    /**
     * Converts the given amount from the first currency to the second currency
     * @param data
     *  Object containing which the source currency is, which the converted currency is and what is the amount of the
     *  first currency
     * @return
     *  The given amount of the first currency converted to the second currency
     */
    @PostMapping
    @RequestMapping("/convert")
    public double convert(@Valid @RequestBody CurrencyConversionData data) {
        return exchangeService.getRate(data.from(), data.to(), data.amount());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid parameter");
        if (ex.getParameter().getParameterType().isAssignableFrom(Currency.class)) {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, String.format("Unsupported currency '%s'", ex.getValue()));
        }

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.status());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<?> handleInvalidData(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (var error : ex.getBindingResult().getAllErrors()) {
            errors.add(error.getDefaultMessage());
        }
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, errors.toArray(new String[0]));
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.status());
    }
}