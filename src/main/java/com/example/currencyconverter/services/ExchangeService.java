package com.example.currencyconverter.services;

import com.example.currencyconverter.clients.CurrencyApiClient;
import com.example.currencyconverter.entities.ConversionEntity;
import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.repositories.ConversionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for exchange functions
 *
 * @author Ivaylo Zahariev
 */
@Service("exchangeService")
public class ExchangeService {

    private final CurrencyApiClient currencyApiClient;
    private final ConversionRepository conversionRepository;

    public ExchangeService(CurrencyApiClient currencyApiClient, ConversionRepository conversionRepository) {
        this.currencyApiClient = currencyApiClient;
        this.conversionRepository = conversionRepository;
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
    public BigDecimal getRate(Currency from, Currency to, BigDecimal amount, boolean saveTransaction)
    {
        if (amount.compareTo(BigDecimal.ONE) < 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }
        if (saveTransaction) {
            ConversionEntity conversionEntity = new ConversionEntity();
            conversionEntity.setFromCurrency(from);
            conversionEntity.setToCurrency(to);
            conversionEntity.setAmount(amount);
            conversionEntity.setEpochMilliseconds(System.currentTimeMillis());
            conversionRepository.save(conversionEntity);
        }
        return currencyApiClient.convert(from, to, amount);
    }
}
