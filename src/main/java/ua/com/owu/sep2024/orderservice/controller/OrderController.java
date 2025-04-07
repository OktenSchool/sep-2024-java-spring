package ua.com.owu.sep2024.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.owu.sep2024.orderservice.dto.order.CreateOrderDto;
import ua.com.owu.sep2024.orderservice.dto.order.OrderDto;
import ua.com.owu.sep2024.orderservice.dto.order.UpdateOrderDto;
import ua.com.owu.sep2024.orderservice.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public List<OrderDto> getOrders(@RequestParam(required = false) BigDecimal minTotalAmount,
                                    @RequestParam(required = false) BigDecimal maxTotalAmount) {
        return orderService.getOrders(minTotalAmount, maxTotalAmount);
    }

    @PostMapping("/orders")
    public OrderDto createOrder(@Valid @RequestBody CreateOrderDto createOrderDto) {
        return orderService.createOrder(createOrderDto);
    }

    @PutMapping("/orders/{id}")
    public Optional<OrderDto> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody UpdateOrderDto updateOrderDto) {
        return orderService.updateOrder(id, updateOrderDto);
    }

    @PatchMapping("/orders/{id}")
    public Optional<OrderDto> patchOrder(@PathVariable("id") Long id, @RequestBody UpdateOrderDto updateOrderDto) {
        return orderService.patchOrder(id, updateOrderDto);
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
    }
}
