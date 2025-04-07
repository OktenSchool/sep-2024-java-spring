package ua.com.owu.sep2024.orderservice.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.com.owu.sep2024.orderservice.service.ProductService;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrintProductsCountJob {

    private final ProductService productService;

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void run() {
        log.info("Products count: {}", productService.getAllProducts(null).size());
    }

    // fixedDelay = 1 hour
    // 1 run - 13:00 - 13:05
    // 2 run - 14:05 - 14:10
    // 3 run - 15:10 - 15:15

    // fixedRate = 1 hour
    // 1 run - 13:00 - 13:05
    // 2 run - 14:00 - 14:05
    // 3 run - 15:00 - 15:05
}
