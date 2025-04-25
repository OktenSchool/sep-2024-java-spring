package ua.com.owu.sep2024.orderservice.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.owu.sep2024.orderservice.api.rest.model.CreateOrderDto;
import ua.com.owu.sep2024.orderservice.api.rest.model.CreateOrderItemDto;
import ua.com.owu.sep2024.orderservice.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerIT {

    static GrantedAuthority SHOP_MANAGER = new SimpleGrantedAuthority("SHOP_MANAGER");

    @MockitoBean
    OrderService orderService;

    @Autowired
    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<CreateOrderDto> createOrderDtoArgumentCaptor;

    @SneakyThrows
    @Test
    void createOrder() {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "shopId": "123",
                                  "items": [
                                    {
                                      "price": 123.45,
                                      "quantity": 1
                                    }
                                  ]
                                }
                                """)
                        .with(jwt().authorities(SHOP_MANAGER)))
                .andExpect(status().isOk());

        verify(orderService).createOrder(createOrderDtoArgumentCaptor.capture());

        var expectedCreateOrderDto = new CreateOrderDto()
                .shopId("123")
                .items(List.of(new CreateOrderItemDto()
                        .price(new BigDecimal("123.45"))
                        .quantity(1)));

        assertEquals(expectedCreateOrderDto, createOrderDtoArgumentCaptor.getValue());
    }
}