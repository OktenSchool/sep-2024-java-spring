package ua.com.owu.sep2024.orderservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemDto(
        BigDecimal productPrice,
        Integer quantity
) {
}
