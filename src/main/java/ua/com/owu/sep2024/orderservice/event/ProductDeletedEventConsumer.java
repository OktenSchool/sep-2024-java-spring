package ua.com.owu.sep2024.orderservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import ua.com.owu.sep2024.orderservice.service.OrderService;
import ua.com.owu.sep2024.productservice.event.ProductDeletedEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductDeletedEventConsumer implements MessageListener<Integer, ProductDeletedEvent> {

    private final OrderService orderService;

    @KafkaListener(topics = "${topics.product-deleted}", groupId = "order-service-group")
    @Override
    public void onMessage(ConsumerRecord<Integer, ProductDeletedEvent> data) {
        ProductDeletedEvent event = data.value();
        log.info("Received event: {}", event);

        orderService.getOrdersByProductId(event.productId()).forEach(order -> {
            orderService.updateOrderStatus(order.getId(), "CANCELLED");
            log.info("Order {} was cancelled as product in it had been deleted", order.getId());
        });
    }
}
