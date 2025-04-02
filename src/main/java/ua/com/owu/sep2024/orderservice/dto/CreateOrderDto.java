package ua.com.owu.sep2024.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderDto(
        @Valid @Size(min = 1, max = 10) @NotEmpty List<CreateOrderItemDto> items
) {
}
