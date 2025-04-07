package ua.com.owu.sep2024.orderservice.dto;

import lombok.Builder;

@Builder
public record MailDto(
        String to,
        String title,
        String message
) {
}
