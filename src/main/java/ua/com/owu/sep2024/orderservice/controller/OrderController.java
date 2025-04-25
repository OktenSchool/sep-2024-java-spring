package ua.com.owu.sep2024.orderservice.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import ua.com.owu.sep2024.orderservice.api.rest.controller.OrderApi;
import ua.com.owu.sep2024.orderservice.api.rest.model.CreateOrderDto;
import ua.com.owu.sep2024.orderservice.api.rest.model.OrderDto;
import ua.com.owu.sep2024.orderservice.api.rest.model.UpdateOrderDto;
import ua.com.owu.sep2024.orderservice.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @RolesAllowed("SHOP_MANAGER")
    @PreAuthorize("hasRole('SHOP_MANAGER')")
    @Override
    public ResponseEntity<OrderDto> createOrder(CreateOrderDto createOrderDto) {
        return ResponseEntity.ok(orderService.createOrder(createOrderDto));
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<OrderDto>> getOrders(BigDecimal minTotalAmount, BigDecimal maxTotalAmount) {
        return ResponseEntity.ok(orderService.getOrders(minTotalAmount, maxTotalAmount));
    }

    @Override
    public ResponseEntity<OrderDto> patchOrder(Long id, UpdateOrderDto updateOrderDto) {
        return ResponseEntity.of(orderService.patchOrder(id, updateOrderDto));
    }

    @Override
    public ResponseEntity<OrderDto> updateOrder(Long id, UpdateOrderDto updateOrderDto) {
        return ResponseEntity.of(orderService.updateOrder(id, updateOrderDto));
    }
}
