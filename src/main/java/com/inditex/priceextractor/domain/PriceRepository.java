package com.inditex.priceextractor.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PriceRepository extends CrudRepository<Price, Long> {
    Optional<Price> findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
            long productId,
            long brandId,
            @NonNull Date applicationDateAgainstStartDate,
            @NonNull Date applicationDateAgainstEndDate
            );
}
