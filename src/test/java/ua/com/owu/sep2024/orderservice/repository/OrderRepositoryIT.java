package ua.com.owu.sep2024.orderservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ua.com.owu.sep2024.orderservice.config.TestcontainersConfig;
import ua.com.owu.sep2024.orderservice.entity.InvoiceEntity;
import ua.com.owu.sep2024.orderservice.entity.OrderEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest(classes = TestcontainersConfig.class)
//@Transactional
@DataJpaTest
@ContextConfiguration(classes = TestcontainersConfig.class)
@ActiveProfiles("test")
class OrderRepositoryIT {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void findAllByInvoiceTotalAmountGreaterThanEqual() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus("ACTIVE");
        orderEntity.setOrderDate(Instant.now());
        orderEntity.setInvoice(InvoiceEntity.builder()
                .totalAmount(new BigDecimal("10000"))
                .build());

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        List<OrderEntity> fetchedOrders = orderRepository.findAllByInvoiceTotalAmountGreaterThanEqual(new BigDecimal("10000"));

        assertThat(fetchedOrders)
                .isNotEmpty()
                .contains(savedOrder);
    }

    @Test
    void findAll() {
        List<OrderEntity> orders = orderRepository.findAll();

        assertThat(orders).isEmpty();
    }
}