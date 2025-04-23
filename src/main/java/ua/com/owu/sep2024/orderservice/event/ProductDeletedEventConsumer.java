package ua.com.owu.sep2024.orderservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.com.owu.sep2024.orderservice.api.event.consumer.IOnProductDeletedEventConsumerService;
import ua.com.owu.sep2024.orderservice.api.event.model.ProductDeletedEventPayload;
import ua.com.owu.sep2024.orderservice.service.OrderService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductDeletedEventConsumer implements IOnProductDeletedEventConsumerService {

    private final OrderService orderService;

    @Override
    public void onProductDeletedEvent(ProductDeletedEventPayload payload, ProductDeletedEventPayloadHeaders headers) {
        log.info("Received event: {}", payload);

        orderService.getOrdersByProductId(payload.getProductId()).forEach(order -> {
            orderService.updateOrderStatus(order.getId(), "CANCELLED");
            log.info("Order {} was cancelled as product in it had been deleted", order.getId());
        });
    }
}
