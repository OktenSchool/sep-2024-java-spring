package ua.com.owu.sep2024.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.owu.sep2024.orderservice.entity.OrderEntity;
import ua.com.owu.sep2024.orderservice.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class OrderController {

    // CRUD operations - create/read/update/delete
    // GET /orders - returns all orders
    // GET /orders/{id} - returns order by id
    // POST /orders - creates new order
    // PUT /orders/{id} - update existing order
    // PATCH /orders/{id} - partial update existing order (updates all non-null fields from request body)
    // DELETE /orders/{id} - delete order by id

    private final OrderRepository orderRepository;

    @GetMapping("/orders")
    public List<OrderEntity> getOrders(@RequestParam(required = false) BigDecimal minTotalAmount,
                                       @RequestParam(required = false) BigDecimal maxTotalAmount) {
        if (minTotalAmount != null && maxTotalAmount != null) {
            return orderRepository.findAllByInvoiceTotalAmountGreaterThanEqualAndInvoiceTotalAmountLessThanEqual(minTotalAmount, maxTotalAmount);
        } else if (minTotalAmount != null) {
            return orderRepository.findAllByInvoiceTotalAmountGreaterThanEqual(minTotalAmount);
        } else if (maxTotalAmount != null) {
            return orderRepository.findAllByInvoiceTotalAmountLessThanEqual(maxTotalAmount);
        } else {
            return orderRepository.findAll();
        }
    }

    @PostMapping("/orders")
    public OrderEntity createOrder(@RequestBody OrderEntity order) {
        order.setOrderDate(Instant.now());

        if (order.getInvoice() != null) {
            order.getInvoice().setInvoiceDate(Instant.now());
        }

        return orderRepository.save(order);
    }

    @PutMapping("/orders/{id}")
    public Optional<OrderEntity> updateOrder(@PathVariable("id") Long id, @RequestBody OrderEntity order) {
        return orderRepository.findById(id)
                .map(existingOrderEntity -> {
                    existingOrderEntity.setStatus(order.getStatus());
                    existingOrderEntity.setInvoice(order.getInvoice());
                    existingOrderEntity.setOrderItems(order.getOrderItems());
                    existingOrderEntity.setInvoice(order.getInvoice());

                    if (existingOrderEntity.getInvoice() != null) {
                        existingOrderEntity.getInvoice().setInvoiceDate(Instant.now());
                    }

                    return orderRepository.save(existingOrderEntity);
                });
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrder(@PathVariable("id") Long id) {
        orderRepository.deleteById(id);
    }
}
