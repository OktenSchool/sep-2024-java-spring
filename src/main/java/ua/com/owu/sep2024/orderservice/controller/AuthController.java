package ua.com.owu.sep2024.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.owu.sep2024.orderservice.dto.auth.SignRequestDto;
import ua.com.owu.sep2024.orderservice.dto.auth.TokenResponse;
import ua.com.owu.sep2024.orderservice.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signUp(@RequestBody SignRequestDto signRequestDto) {
        return ResponseEntity.ok(TokenResponse.builder()
                .accessToken(userService.register(signRequestDto)).build());
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> signIn(@RequestBody SignRequestDto signRequestDto) {
        return ResponseEntity.ok(TokenResponse.builder()
                .accessToken(userService.login(signRequestDto)).build());
    }
}
