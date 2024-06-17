package com.example.currencyconverter.services;

import com.example.currencyconverter.clients.CurrencyApiClient;
import com.example.currencyconverter.entities.ConversionEntity;
import com.example.currencyconverter.models.ConversionResult;
import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.repositories.ConversionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

    @Mock
    private CurrencyApiClient mockCurrencyApiClient;
    @Mock
    private ConversionRepository mockConversionRepository;

    @Test
    public void givenValidCurrencies_whenRetrievingRate_thenReturnExchangeRate() {
        when(
                mockCurrencyApiClient.convert(
                        Mockito.any(Currency.class),
                        Mockito.any(Currency.class),
                        Mockito.any()
                )
        ).thenReturn(BigDecimal.valueOf(1.95));

        ExchangeService exchangeService = new ExchangeService(mockCurrencyApiClient, mockConversionRepository);
        Assertions.assertEquals(
                BigDecimal.valueOf(1.95),
                exchangeService.getRate(Currency.EUR, Currency.BGN, BigDecimal.ONE),
                "Incorrect exchange rate");
    }

    @Test
    public void givenInvalidAmount_whenRetrievingRate_thenThrowException() {
        ExchangeService exchangeService = new ExchangeService(new CurrencyApiClient(), mockConversionRepository);
        Assertions.assertThrows(
                RuntimeException.class,
                () -> exchangeService.getRate(Currency.EUR, Currency.BGN, BigDecimal.ZERO),
                "Invalid amount should not be accepted"
        );
    }

    @Test
    public void givenValidData_whenConvertingCurrencies_thenReturnConvertedResult() {
        when(
                mockCurrencyApiClient.convert(
                        Mockito.any(Currency.class),
                        Mockito.any(Currency.class),
                        Mockito.any()
                )
        ).thenReturn(BigDecimal.valueOf(195));

        long timeMillis = System.currentTimeMillis();
        ConversionEntity conversionEntity = new ConversionEntity();
        conversionEntity.setId(Constants.UUID_CONSTANT);
        conversionEntity.setFromCurrency(Currency.EUR);
        conversionEntity.setToCurrency(Currency.BGN);
        conversionEntity.setSourceAmount(BigDecimal.valueOf(100));
        conversionEntity.setConvertedAmount(BigDecimal.valueOf(195));
        conversionEntity.setEpochMilliseconds(timeMillis);
        when(mockConversionRepository.save(Mockito.any()))
                .thenReturn(conversionEntity);

        ExchangeService exchangeService = new ExchangeService(mockCurrencyApiClient, mockConversionRepository);
        ConversionResult conversionResult = exchangeService.convert(Currency.EUR, Currency.BGN, BigDecimal.valueOf(100));
        Assertions.assertEquals(
                Constants.UUID_CONSTANT,
                conversionResult.id(),
                "Incorrect conversion ID"
        );
        Assertions.assertEquals(
                BigDecimal.valueOf(195),
                conversionResult.value(),
                "Incorrect conversion result"
        );
    }

    @Test
    public void givenInvalidAmount_whenConvertingCurrencies_thenThrowException() {
        ExchangeService exchangeService = new ExchangeService(new CurrencyApiClient(), mockConversionRepository);
        Assertions.assertThrows(
                RuntimeException.class,
                () -> exchangeService.convert(Currency.EUR, Currency.BGN, BigDecimal.ZERO),
                "Invalid amount should not be accepted"
        );
    }

    private static class Constants {
        public static UUID UUID_CONSTANT = UUID.fromString("5c8449a5-2633-48cf-afc1-fb364c8c0fd5");
    }
}