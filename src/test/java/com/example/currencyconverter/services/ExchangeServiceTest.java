package com.example.currencyconverter.services;

import com.example.currencyconverter.clients.CurrencyApiClient;
import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.repositories.ConversionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

    @Mock
    private CurrencyApiClient mockCurrencyApiClient;
    @Mock
    private ConversionRepository conversionRepository;

    @Test
    public void givenValidCurrencies_whenRetrievingRate_thenReturnExchangeRate() {
        when(
                mockCurrencyApiClient.convert(
                        Mockito.any(Currency.class),
                        Mockito.any(Currency.class),
                        Mockito.any()
                )
        ).thenReturn(BigDecimal.valueOf(1.95));

        ExchangeService exchangeService = new ExchangeService(mockCurrencyApiClient, conversionRepository);
        Assertions.assertEquals(
                BigDecimal.valueOf(1.95),
                exchangeService.getRate(Currency.EUR, Currency.BGN, BigDecimal.ONE, false),
                "Incorrect exchange rate");
    }

    @Test
    public void givenInvalidAmount_whenRetrievingRate_thenThrowException() {
        ExchangeService exchangeService = new ExchangeService(new CurrencyApiClient(), conversionRepository);
        Assertions.assertThrows(
                RuntimeException.class,
                () -> exchangeService.getRate(Currency.EUR, Currency.BGN, BigDecimal.ZERO, false),
                "Invalid amount should not be accepted"
        );
    }
}