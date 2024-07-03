package com.example.currencyconverter.dtos;

import com.example.currencyconverter.models.Currency;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for conversion transaction
 *
 * @author Ivaylo Zahariev
 */
public record Conversion(UUID id, Currency fromCurrency, Currency toCurrency, BigDecimal sourceAmount,
                         BigDecimal convertedAmount, Long epochMilliseconds) {
}
