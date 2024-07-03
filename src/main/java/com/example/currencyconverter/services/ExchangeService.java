package com.example.currencyconverter.services;

import com.example.currencyconverter.clients.CurrencyApiClient;
import com.example.currencyconverter.dtos.Conversion;
import com.example.currencyconverter.entities.ConversionEntity;
import com.example.currencyconverter.errors.InvalidAmountException;
import com.example.currencyconverter.errors.InvalidPageNumberException;
import com.example.currencyconverter.errors.InvalidPageSizeException;
import com.example.currencyconverter.models.ConversionResult;
import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.repositories.ConversionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public BigDecimal getRate(Currency from, Currency to) {
        return currencyApiClient.convert(from, to, BigDecimal.ONE);
    }

    /**
     * Converts a given amount from one currency to another
     * @param from
     *  The source currency
     * @param to
     *  The converted currency
     * @param amount
     *  The amount of the source currency
     * @return
     *  The converted amount
     * @throws InvalidAmountException
     *  Thrown if the given amount is less than 0
     */
    public ConversionResult convert(Currency from, Currency to, BigDecimal amount) throws InvalidAmountException {
        if (amount.compareTo(BigDecimal.ONE) < 0) {
            throw new InvalidAmountException();
        }
        BigDecimal convertedValue = BigDecimal.ZERO;
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            convertedValue = currencyApiClient.convert(from, to, amount);
        }
        ConversionEntity conversionEntity = new ConversionEntity();
        conversionEntity.setFromCurrency(from);
        conversionEntity.setToCurrency(to);
        conversionEntity.setSourceAmount(amount);
        conversionEntity.setConvertedAmount(convertedValue);
        conversionEntity.setEpochMilliseconds(System.currentTimeMillis());
        ConversionEntity savedEntity = conversionRepository.save(conversionEntity);

        return new ConversionResult(savedEntity.getId(), convertedValue);
    }

    public Conversion getConversion(UUID id) {
        Optional<ConversionEntity> conversionOptional = conversionRepository.findById(id);
        return conversionOptional.isPresent() ? conversionOptional.get().toConversion() : null;
    }

    public List<Conversion> conversionHistoryByDate(Date date, int page, int size) throws
            InvalidPageNumberException, InvalidPageSizeException {
        if (page < 0) {
            throw new InvalidPageNumberException();
        }
        if (size < 1) {
            throw new InvalidPageSizeException();
        }
        long startOfDateDay = date.getTime();
        long endOfDateDay = startOfDateDay + 86400000; // Start of day + 24 hours
        return conversionRepository
                .findByEpochMillisecondsBetween(startOfDateDay, endOfDateDay, PageRequest.of(page, size))
                .stream()
                .map(ce -> ce.toConversion())
                .toList();
    }
}
