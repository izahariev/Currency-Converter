package com.example.currencyconverter.services;

import com.example.currencyconverter.clients.CurrencyApiClient;
import com.example.currencyconverter.entities.ConversionEntity;
import com.example.currencyconverter.models.ConversionResult;
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
    public BigDecimal getRate(Currency from, Currency to, BigDecimal amount)
    {
        if (amount.compareTo(BigDecimal.ONE) < 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }
        return currencyApiClient.convert(from, to, amount);
    }

    public ConversionResult convert(Currency from, Currency to, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ONE) < 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }
        BigDecimal convertedValue = currencyApiClient.convert(from, to, amount);
        ConversionEntity conversionEntity = new ConversionEntity();
        conversionEntity.setFromCurrency(from);
        conversionEntity.setToCurrency(to);
        conversionEntity.setSourceAmount(amount);
        conversionEntity.setConvertedAmount(convertedValue);
        conversionEntity.setEpochMilliseconds(System.currentTimeMillis());
        ConversionEntity savedEntity = conversionRepository.save(conversionEntity);

        return new ConversionResult(savedEntity.getId(), convertedValue);
    }
}
