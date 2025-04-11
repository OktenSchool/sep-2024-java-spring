package ua.com.owu.sep2024.orderservice.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {

    public Set<String> getAssignedShopIds() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        Map<String, Object> attributes = jwt.getClaimAsMap("attributes");

        if (attributes == null) {
            return Set.of();
        }

        List<String> roles = (List<String>) attributes.getOrDefault("user_assigned_shop_ids", List.<String>of());

        return new HashSet<>(roles);
    }
}
