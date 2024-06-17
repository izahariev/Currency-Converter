package com.example.currencyconverter.models;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Model for currency conversion result
 * @param id
 *  ID of the conversion transaction
 * @param result
 *  Converted value
 *
 * @author Ivaylo Zahariev
 */
public record ConversionResult(UUID id, BigDecimal value) {
}
