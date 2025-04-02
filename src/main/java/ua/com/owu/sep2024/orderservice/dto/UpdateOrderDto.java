package ua.com.owu.sep2024.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateOrderDto(
        @NotBlank String status,
        @Valid UpsertInvoiceDto invoice,
        @Valid @Size(min = 1, max = 10) @NotEmpty List<CreateOrderItemDto> items
) {
}
