package ua.com.owu.sep2024.orderservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class InfoConfiguration {

    @Bean
    public Info info() {
        return Info.builder()
                .address("Ukraine")
                .phone("+91123456789").build();
    }

    @Bean
    @Primary
    public Info defaultInfo() {
        return Info.builder()
                .address("Kyiv")
                .phone("+12313232").build();
    }
}
