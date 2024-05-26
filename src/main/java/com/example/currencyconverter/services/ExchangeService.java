package com.example.currencyconverter.services;

import com.example.currencyconverter.clients.CurrencyApiClient;
import com.example.currencyconverter.models.Currency;
import org.springframework.stereotype.Service;

/**
 * Service for exchange functions
 *
 * @author Ivaylo Zahariev
 */
@Service("exchangeService")
public class ExchangeService {

    private final CurrencyApiClient currencyApiClient;

    public ExchangeService(CurrencyApiClient currencyApiClient) {
        this.currencyApiClient = currencyApiClient;
    }

    /**
     * Convert the amount of the first given currency to its equivalent in the second given currency
     * @param from
     *  The source currency
     * @param to
     *  The convert currency
     * @param amount
     *  The source currency amount
     */
    public double getRate(Currency from, Currency to, double amount)
    {
        if (amount < 1) {
            throw new RuntimeException("Amount must be greater than 0");
        }
        return currencyApiClient.convert(from, to, amount);
    }
}
