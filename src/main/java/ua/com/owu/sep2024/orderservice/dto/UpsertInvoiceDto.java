package ua.com.owu.sep2024.orderservice.dto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record UpsertInvoiceDto(
        String number,
        @DecimalMin(value = "0.0", inclusive = false) BigDecimal totalAmount
) {
}
