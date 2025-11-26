package com.example.datawarehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;

public record FxDealRequestDTO(
        @NotBlank String dealUniqueId,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$", message = "must be a valid 3-letter uppercase ISO currency code")
        String fromCurrencyIsoCode,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$", message = "must be a valid 3-letter uppercase ISO currency code")
        String toCurrencyIsoCode,

        @NotNull Instant dealTimestamp,
        @NotNull @Positive BigDecimal dealAmount
) {}
