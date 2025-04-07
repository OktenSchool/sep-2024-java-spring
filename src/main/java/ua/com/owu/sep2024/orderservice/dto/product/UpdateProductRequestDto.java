package ua.com.owu.sep2024.orderservice.dto.product;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UpdateProductRequestDto(
        String name,
        BigDecimal price
) {
}
