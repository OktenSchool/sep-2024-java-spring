package ua.com.owu.sep2024.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.owu.sep2024.orderservice.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findAllByInvoiceTotalAmountGreaterThanEqualAndInvoiceTotalAmountLessThanEqual(BigDecimal minimumAmount, BigDecimal maximumAmount);

    @Query(value = """
            SELECT o.* FROM orders o
            LEFT JOIN invoices i ON i.id = o.invoice_id
            WHERE i.total_amount >= :minimumAmount
            """, nativeQuery = true)
    List<OrderEntity> findAllByInvoiceTotalAmountGreaterThanEqual(BigDecimal minimumAmount);

    @Query("""
            SELECT o FROM OrderEntity o
            LEFT JOIN InvoiceEntity i on o.invoice.id = i.id
            WHERE i.totalAmount <= :maximumAmount
            """)
    List<OrderEntity> findAllByInvoiceTotalAmountLessThanEqual(BigDecimal maximumAmount);
}
