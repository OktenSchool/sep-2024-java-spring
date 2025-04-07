package ua.com.owu.sep2024.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.owu.sep2024.orderservice.dto.order.CreateOrderDto;
import ua.com.owu.sep2024.orderservice.dto.order.OrderDto;
import ua.com.owu.sep2024.orderservice.dto.order.UpdateOrderDto;
import ua.com.owu.sep2024.orderservice.entity.OrderEntity;
import ua.com.owu.sep2024.orderservice.mapper.OrderMapper;
import ua.com.owu.sep2024.orderservice.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public List<OrderDto> getOrders(BigDecimal minTotalAmount, BigDecimal maxTotalAmount) {
        List<OrderEntity> orders;

        if (minTotalAmount != null && maxTotalAmount != null) {
            orders = orderRepository.findAllByInvoiceTotalAmountGreaterThanEqualAndInvoiceTotalAmountLessThanEqual(minTotalAmount, maxTotalAmount);
        } else if (minTotalAmount != null) {
            orders = orderRepository.findAllByInvoiceTotalAmountGreaterThanEqual(minTotalAmount);
        } else if (maxTotalAmount != null) {
            orders = orderRepository.findAllByInvoiceTotalAmountLessThanEqual(maxTotalAmount);
        } else {
            orders = orderRepository.findAll();
        }

        return orders.stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    public OrderDto createOrder(CreateOrderDto createOrderDto) {
        OrderEntity order = orderMapper.createOrder(createOrderDto);
        order.assignOrderItems();
        OrderEntity savedOrder = orderRepository.save(order);
        return orderMapper.toOrderDto(savedOrder);
    }

    @Transactional
    public Optional<OrderDto> updateOrder(Long orderId, UpdateOrderDto updateOrderDto) {
        return orderRepository.findById(orderId)
                .map(existingOrder -> orderMapper.updateOrderEntity(existingOrder, updateOrderDto))
                .map(OrderEntity::assignOrderItems)
                .map(orderMapper::toOrderDto);
    }

    @Transactional
    public Optional<OrderDto> patchOrder(Long orderId, UpdateOrderDto updateOrderDto) {
        return orderRepository.findById(orderId)
                .map(existingOrder -> orderMapper.patchOrderEntity(existingOrder, updateOrderDto))
                .map(OrderEntity::assignOrderItems)
                .map(orderMapper::toOrderDto);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
