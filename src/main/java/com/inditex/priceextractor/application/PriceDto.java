package com.inditex.priceextractor.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inditex.priceextractor.domain.Price;
import jakarta.validation.constraints.NotNull;

import java.text.SimpleDateFormat;


public record PriceDto(
        @JsonProperty("price_list") long priceId,
        @JsonProperty("product_id") long productId,
        @JsonProperty("brand_id") long brandId,
        @NotNull @JsonProperty("start_date") String startDate,
        @NotNull@JsonProperty("end_date") String endDate,
        @JsonProperty("price") double price
) {
    public static PriceDto fromPrice(SimpleDateFormat simpleDateFormat, Price price) {

        return new PriceDto(
                price.getId(),
                price.getProductId(),
                price.getBrandId(),
                simpleDateFormat.format(price.getStartDate()),
                simpleDateFormat.format(price.getEndDate()),
                price.getPrice()
        );
    }

}
