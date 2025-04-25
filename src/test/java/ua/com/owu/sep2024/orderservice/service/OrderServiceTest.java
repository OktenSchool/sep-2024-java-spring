package ua.com.owu.sep2024.orderservice.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.owu.sep2024.orderservice.api.rest.model.CreateOrderDto;
import ua.com.owu.sep2024.orderservice.api.rest.model.OrderDto;
import ua.com.owu.sep2024.orderservice.api.rest.model.OrderItemDto;
import ua.com.owu.sep2024.orderservice.entity.InvoiceEntity;
import ua.com.owu.sep2024.orderservice.entity.OrderEntity;
import ua.com.owu.sep2024.orderservice.entity.OrderItemEntity;
import ua.com.owu.sep2024.orderservice.exception.ShopIsNotAccessibleException;
import ua.com.owu.sep2024.orderservice.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserService userService;

    @Spy
    OrderMapper orderMapper = new OrderMapperImpl();

    @InjectMocks
    OrderService orderService;

    @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("before each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("after each");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }

    @Test
    void testGetOrders() {
        var orderEntity = OrderEntity.builder()
                .id(1L)
                .orderDate(Instant.now())
                .status("ACTIVE")
                .invoice(InvoiceEntity.builder()
                        .id(2L)
                        .invoiceDate(Instant.now())
                        .invoiceNumber("#1234")
                        .totalAmount(new BigDecimal("123.45")).build())
                .orderItems(List.of(OrderItemEntity.builder()
                        .id(3L)
                        .price(new BigDecimal("123.45"))
                        .quantity(1).build()))
                .build();

        when(orderRepository.findAllByInvoiceTotalAmountGreaterThanEqualAndInvoiceTotalAmountLessThanEqual(any(), any()))
                .thenReturn(List.of(orderEntity));

        var result = orderService.getOrders(new BigDecimal("10"), new BigDecimal("20"));

        assertThat(result)
                .isNotEmpty()
                .contains(new OrderDto()
                        .id(1L)
                        .status("ACTIVE")
                        .orderDate(orderEntity.getOrderDate().atOffset(ZoneOffset.UTC))
                        .totalAmount(orderEntity.getInvoice().getTotalAmount())
                        .items(List.of(
                                new OrderItemDto()
                                        .productPrice(new BigDecimal("123.45"))
                                        .quantity(1)
                        )));

        verify(orderRepository).findAllByInvoiceTotalAmountGreaterThanEqualAndInvoiceTotalAmountLessThanEqual(new BigDecimal("10"), new BigDecimal("20"));
        verifyNoInteractions(userService);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void createOrder_ifUserAssignedShopIdsDoesNotContainShopId_shouldThrowException() {
        when(userService.getAssignedShopIds()).thenReturn(Set.of("1", "2"));

        var createOrderDto = new CreateOrderDto()
                .shopId("3");

        assertThatThrownBy(() -> orderService.createOrder(createOrderDto))
                .isInstanceOf(ShopIsNotAccessibleException.class)
                .hasMessage("Shop id 3 is not accessible");

        verifyNoInteractions(orderRepository);
    }

    static Stream<Arguments> minMax() {
        return Stream.of(
                arguments(null, new BigDecimal("20")),
                arguments(new BigDecimal("10"), null),
                arguments(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("minMax")
    void getOrders_shouldNotThrowExceptionForAnyArgumentValues(BigDecimal min, BigDecimal max) {
        assertDoesNotThrow(() -> orderService.getOrders(min, max));
    }
}