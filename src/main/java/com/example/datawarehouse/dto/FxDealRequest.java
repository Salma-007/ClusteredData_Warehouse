package com.example.datawarehouse.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FxDealRequest {

    @NotBlank(message = "Deal Unique ID is required")
    @Size(max = 100, message = "Deal Unique ID must not exceed 100 characters")
    private String dealUniqueId;

    @NotBlank(message = "From Currency ISO Code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "From Currency must be a valid 3-letter ISO code")
    private String fromCurrencyIsoCode;

    @NotBlank(message = "To Currency ISO Code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "To Currency must be a valid 3-letter ISO code")
    private String toCurrencyIsoCode;

    @NotNull(message = "Deal Timestamp is required")
    private LocalDateTime dealTimestamp;

    @NotNull(message = "Deal Amount is required")
    @DecimalMin(value = "0.0001", inclusive = true, message = "Deal Amount must be positive")
    @Digits(integer = 15, fraction = 4, message = "Deal Amount format is invalid")
    private BigDecimal dealAmount;
}
