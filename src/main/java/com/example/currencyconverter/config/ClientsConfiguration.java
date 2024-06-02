package com.example.currencyconverter.config;

import com.example.currencyconverter.clients.CurrencyApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientsConfiguration {

    @Bean
    public CurrencyApiClient getCurrencyApiClient() {
        return new CurrencyApiClient();
    }
}
