package com.example.currencyconverter.repositories;

import com.example.currencyconverter.entities.ConversionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Ivaylo Zahariev
 */
public interface ConversionRepository extends JpaRepository<ConversionEntity, UUID> {
}
