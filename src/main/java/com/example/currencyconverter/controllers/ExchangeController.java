package com.example.currencyconverter.controllers;

import com.example.currencyconverter.dtos.Conversion;
import com.example.currencyconverter.errors.InvalidAmountException;
import com.example.currencyconverter.errors.InvalidPageNumberException;
import com.example.currencyconverter.errors.InvalidPageSizeException;
import com.example.currencyconverter.models.ApiResponse;
import com.example.currencyconverter.models.ConversionResult;
import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.models.CurrencyConversionData;
import com.example.currencyconverter.services.ExchangeService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    public ApiResponse<BigDecimal> getRate(@RequestParam Currency from, @RequestParam Currency to) {
        return new ApiResponse<>(HttpStatus.OK, exchangeService.getRate(from, to));
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
    public ApiResponse<ConversionResult> convert(@Valid @RequestBody CurrencyConversionData data) {
        try {
            return new ApiResponse<>(HttpStatus.OK, exchangeService.convert(data.from(), data.to(), data.amount()));
        } catch (InvalidAmountException e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, null, e.getMessage());
        }
    }

    @GetMapping
    @RequestMapping("/get-transaction")
    public ApiResponse<Conversion> getTransaction(@RequestParam UUID id) {
        if (id == null) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, null, "Transaction ID is required");
        }
        return new ApiResponse<>(HttpStatus.OK, exchangeService.getConversion(id));
    }

    @GetMapping
    @RequestMapping("/get-transactions")
    public ApiResponse<List<Conversion>> getConversionHistory(@RequestParam Date date, int page, int size) {

        if (date == null) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, null, "Transaction date is required");
        }

        try {
            return new ApiResponse<>(HttpStatus.OK, exchangeService.conversionHistoryByDate(date, page, size));
        } catch (InvalidPageNumberException e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, null, e.getMessage());
        } catch (InvalidPageSizeException e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, null, e.getMessage());
        }
    }

    // Exception Handlers

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ApiResponse<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var apiError = new ApiResponse<>(HttpStatus.BAD_REQUEST, null, "Invalid parameter");
        if (ex.getParameter().getParameterType().isAssignableFrom(Currency.class)) {
            apiError = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST,
                    null,
                    String.format("Unsupported currency '%s'", ex.getValue())
            );
        }

        return apiError;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ApiResponse<?> handleIncorrectData(MethodArgumentNotValidException ex) {
        var errors = new ArrayList<>();
        for (var error : ex.getBindingResult().getAllErrors()) {
            errors.add(error.getDefaultMessage());
        }
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, null, errors.toArray(new String[0]));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ApiResponse<?> handleInvalidData(HttpMessageNotReadableException ex) {
        var apiError = new ApiResponse<>(HttpStatus.BAD_REQUEST, null, "Invalid value given");
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
            switch (invalidFormatException.getTargetType().getSimpleName()){
                case "BigDecimal": {
                    apiError =  new ApiResponse<>(
                            HttpStatus.BAD_REQUEST,
                            null,
                            String.format("Invalid value '%s'! Must be a number", invalidFormatException.getValue())
                    );
                    break;
                }
                case "Currency": {
                    apiError =  new ApiResponse<>(
                            HttpStatus.BAD_REQUEST,
                            null,
                            String.format(
                                    "Invalid value '%s'! Must be a one of %s",
                                    invalidFormatException.getValue(),
                                    Arrays.toString(Currency.values())
                            )
                    );
                    break;
                }
            }
        }
        return apiError;
    }
}