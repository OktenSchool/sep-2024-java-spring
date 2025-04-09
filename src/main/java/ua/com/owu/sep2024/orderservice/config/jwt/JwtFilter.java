package ua.com.owu.sep2024.orderservice.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.com.owu.sep2024.orderservice.utils.JwtUtil;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtFilter start");

        String authHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("JwtFilter authHeaderValue: {}", authHeaderValue);

        if (StringUtils.isBlank(authHeaderValue)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeaderValue.substring("Bearer ".length());

        try {
            if (jwtUtil.isTokenExpired(accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtUtil.extractUsername(accessToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            List<GrantedAuthority> roles = jwtUtil.extractFromToken(accessToken, claims -> claims.get("roles", List.class))
                    .stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_%s".formatted(role.toString())))
                    .toList();

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, roles);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
