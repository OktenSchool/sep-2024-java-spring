package ua.com.owu.sep2024.orderservice.dto.order;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record OrderDto(
        Long id,
        String status,
        Instant orderDate,
        BigDecimal totalAmount,
        List<OrderItemDto> items
) {
}
