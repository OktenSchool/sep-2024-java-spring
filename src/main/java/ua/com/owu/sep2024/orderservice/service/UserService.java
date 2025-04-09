package ua.com.owu.sep2024.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.owu.sep2024.orderservice.dto.auth.SignRequestDto;
import ua.com.owu.sep2024.orderservice.entity.UserEntity;
import ua.com.owu.sep2024.orderservice.repository.UserRepository;
import ua.com.owu.sep2024.orderservice.utils.JwtUtil;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username '%s' not found".formatted(username)));
    }

    public String register(SignRequestDto signRequestDto) {
        UserEntity user = UserEntity.builder()
                .username(signRequestDto.name())
                .password(passwordEncoder.encode(signRequestDto.password()))
                .role("USER").build();
        userRepository.save(user);
        return jwtUtil.generateAccessToken(user);
    }

    public String login(SignRequestDto signRequestDto) {
        UserEntity user = userRepository.findByUsername(signRequestDto.name())
                .filter(userEntity -> passwordEncoder.matches(signRequestDto.password(), userEntity.getPassword()))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        return jwtUtil.generateAccessToken(user);
    }
}
