package com.example.currencyconverter.entities;

import com.example.currencyconverter.dtos.Conversion;
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
    private BigDecimal sourceAmount;
    private BigDecimal convertedAmount;
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

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public void setSourceAmount(BigDecimal amount) {
        this.sourceAmount = amount;
    }

    public Long getEpochMilliseconds() {
        return epochMilliseconds;
    }

    public void setEpochMilliseconds(Long epochMilliseconds) {
        this.epochMilliseconds = epochMilliseconds;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public Conversion toConversion() {
        return new Conversion(this.id, this.fromCurrency, this.toCurrency, this.sourceAmount, this.convertedAmount,
                this.epochMilliseconds);
    }
}
