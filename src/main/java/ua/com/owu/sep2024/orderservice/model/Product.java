package ua.com.owu.sep2024.orderservice.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;

@Data
@Document("products")
public class Product {

    @MongoId
    private String id;

    private String name;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;
}
