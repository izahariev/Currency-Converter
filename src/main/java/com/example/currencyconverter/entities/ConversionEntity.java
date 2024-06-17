package com.example.currencyconverter.entities;

import com.example.currencyconverter.models.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Ivaylo Zahariev
 */
@Entity(name = "transactions")
public class ConversionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Currency fromCurrency;
    @Enumerated(EnumType.STRING)
    private Currency toCurrency;
    private BigDecimal amount;
    private Long epochMilliseconds;

    public ConversionEntity() {}

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(Currency from) {
        this.fromCurrency = from;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(Currency to) {
        this.toCurrency = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getEpochMilliseconds() {
        return epochMilliseconds;
    }

    public void setEpochMilliseconds(Long epochMilliseconds) {
        this.epochMilliseconds = epochMilliseconds;
    }
}
