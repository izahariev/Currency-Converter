package com.example.currencyconverter.models;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

/**
 * Model for data needed for a currency conversion
 * @param from
 *  The source currency
 * @param to
 *  The converted currency
 * @param amount
 *  The amount of the source currency. Must be greater than 0
 *
 * @author Ivaylo Zahariev
 */
public record CurrencyConversionData(
        Currency from,
        Currency to,
        @DecimalMin(value = "1.0", message = "The amount must be greater than 0") BigDecimal amount) {
}
