package ua.com.owu.sep2024.orderservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.owu.sep2024.orderservice.model.Product;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findAllByPriceGreaterThanEqual(BigDecimal minPrice);

    @Query("{ price : { $gte : { $numberDecimal : ?0}}}")
    List<Product> findAllByMinPrice(BigDecimal minPrice);
}
