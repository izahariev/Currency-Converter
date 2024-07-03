package com.example.currencyconverter.repositories;

import com.example.currencyconverter.entities.ConversionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for access to conversion data
 *
 * @author Ivaylo Zahariev
 */
public interface ConversionRepository extends JpaRepository<ConversionEntity, UUID> {
    List<ConversionEntity> findByEpochMillisecondsBetween(long startOfDayMilliseconds, long endOfDayMilliseconds,
                                                          Pageable pageable);
}
