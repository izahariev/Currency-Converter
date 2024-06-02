package com.example.currencyconverter.services;

import com.example.currencyconverter.clients.CurrencyApiClient;
import com.example.currencyconverter.models.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

    @Mock
    CurrencyApiClient mockCurrencyApiClient;

    @Test
    public void givenValidCurrencies_whenRetrievingRate_thenReturnExchangeRate() {
        when(
                mockCurrencyApiClient.convert(
                        Mockito.any(Currency.class),
                        Mockito.any(Currency.class),
                        Mockito.anyDouble()
                )
        ).thenReturn(1.95);

        ExchangeService exchangeService = new ExchangeService(mockCurrencyApiClient);
        Assertions.assertEquals(
                1.95,
                exchangeService.getRate(Currency.EUR, Currency.BGN, 1),
                "Incorrect exchange rate");
    }

    @Test
    public void givenInvalidAmount_whenRetrievingRate_thenThrowException() {
        ExchangeService exchangeService = new ExchangeService(new CurrencyApiClient());
        Assertions.assertThrows(
                RuntimeException.class,
                () -> exchangeService.getRate(Currency.EUR, Currency.BGN, 0),
                "Invalid amount should not be accepted"
        );
    }
}