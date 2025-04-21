package ua.com.owu.sep2024.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.owu.sep2024.orderservice.api.rest.model.CreateOrderDto;
import ua.com.owu.sep2024.orderservice.api.rest.model.OrderDto;
import ua.com.owu.sep2024.orderservice.api.rest.model.UpdateOrderDto;
import ua.com.owu.sep2024.orderservice.client.rest.api.ProductApi;
import ua.com.owu.sep2024.orderservice.client.rest.model.ProductResponseDto;
import ua.com.owu.sep2024.orderservice.client.rest.model.SearchProductRequestDto;
import ua.com.owu.sep2024.orderservice.entity.OrderEntity;
import ua.com.owu.sep2024.orderservice.entity.OrderItemEntity;
import ua.com.owu.sep2024.orderservice.exception.ShopIsNotAccessibleException;
import ua.com.owu.sep2024.orderservice.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final UserService userService;

    @Qualifier("serviceAuthProductApi")
    private final ProductApi productApi;

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
        if (!userService.getAssignedShopIds().contains(createOrderDto.getShopId())) {
            throw new ShopIsNotAccessibleException(createOrderDto.getShopId());
        }

        OrderEntity order = orderMapper.createOrder(createOrderDto);

        enrichWithProductInfo(order);

        order.assignOrderItems();
        OrderEntity savedOrder = orderRepository.save(order);
        return orderMapper.toOrderDto(savedOrder);
    }

    private OrderEntity enrichWithProductInfo(OrderEntity order) {
        if (CollectionUtils.isNotEmpty(order.getOrderItems())) {
            List<String> productIds = order.getOrderItems().stream()
                    .map(OrderItemEntity::getProductId)
                    .toList();

            Map<String, ProductResponseDto> products = productApi
                    .searchProducts(new SearchProductRequestDto()
                            .productIds(productIds))
                    .stream()
                    .collect(toMap(ProductResponseDto::getId, Function.identity()));

            order.getOrderItems().forEach(item -> {
                if (!products.containsKey(item.getProductId())) {
                    throw new IllegalArgumentException("Product '%s' not found".formatted(item.getProductId()));
                }

                ProductResponseDto product = products.get(item.getProductId());
                item.setUnitPrice(product.getPrice());
            });
        }

        return order;
    }

    @Transactional
    public Optional<OrderDto> updateOrder(Long orderId, UpdateOrderDto updateOrderDto) {
        return orderRepository.findById(orderId)
                .map(existingOrder -> orderMapper.updateOrderEntity(existingOrder, updateOrderDto))
                .map(OrderEntity::assignOrderItems)
                .map(this::enrichWithProductInfo)
                .map(orderMapper::toOrderDto);
    }

    @Transactional
    public Optional<OrderDto> patchOrder(Long orderId, UpdateOrderDto updateOrderDto) {
        return orderRepository.findById(orderId)
                .map(existingOrder -> orderMapper.patchOrderEntity(existingOrder, updateOrderDto))
                .map(OrderEntity::assignOrderItems)
                .map(this::enrichWithProductInfo)
                .map(orderMapper::toOrderDto);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus) {
        orderRepository.findById(orderId).ifPresent(order -> order.setStatus(newStatus));
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<OrderEntity> getOrdersByProductId(String productId) {
        return orderRepository.findAllByOrderItemsProductId(productId);
    }
}
