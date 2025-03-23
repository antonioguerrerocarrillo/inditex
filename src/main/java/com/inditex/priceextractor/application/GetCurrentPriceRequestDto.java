package com.inditex.priceextractor.application;


import jakarta.validation.constraints.NotNull;

public record GetCurrentPriceRequestDto(
        @NotNull String applicationDate,
        long productId,
        long brandId
) {
}
