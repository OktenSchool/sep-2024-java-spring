package ua.com.owu.sep2024.orderservice.dto.auth;

import lombok.Builder;

@Builder
public record TokenResponse(String accessToken) {
}
