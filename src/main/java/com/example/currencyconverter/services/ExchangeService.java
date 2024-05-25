package com.example.currencyconverter.services;

import com.example.currencyconverter.clients.CurrencyApiClient;
import com.example.currencyconverter.models.Currency;
import org.springframework.stereotype.Service;

@Service("exchangeService")
public class ExchangeService {

    private final CurrencyApiClient currencyApiClient;

    public ExchangeService(CurrencyApiClient currencyApiClient) {
        this.currencyApiClient = currencyApiClient;
    }

    public double getRate(Currency from, Currency to, double amount)
    {
        if (amount < 1) {
            throw new RuntimeException("Amount must be greater than 0");
        }
        return currencyApiClient.convert(from, to, amount);
    }
}
