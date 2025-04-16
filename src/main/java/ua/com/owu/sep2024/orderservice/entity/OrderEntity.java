package ua.com.owu.sep2024.orderservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_status")
    private String status;

    @Column(name = "order_date")
    private Instant orderDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderItemEntity> orderItems;

    public OrderEntity assignOrderItems() {
        if (CollectionUtils.isNotEmpty(orderItems)) {
            orderItems.forEach(item -> item.setOrder(this));
        }
        return this;
    }

    public BigDecimal getTotal() {
        if (CollectionUtils.isNotEmpty(orderItems)) {
            return orderItems.stream()
                    .map(OrderItemEntity::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
