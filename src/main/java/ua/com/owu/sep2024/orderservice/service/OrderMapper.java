package ua.com.owu.sep2024.orderservice.service;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.owu.sep2024.orderservice.api.rest.model.CreateOrderDto;
import ua.com.owu.sep2024.orderservice.api.rest.model.OrderDto;
import ua.com.owu.sep2024.orderservice.api.rest.model.OrderItemDto;
import ua.com.owu.sep2024.orderservice.api.rest.model.UpdateOrderDto;
import ua.com.owu.sep2024.orderservice.entity.OrderEntity;
import ua.com.owu.sep2024.orderservice.entity.OrderItemEntity;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = SPRING, imports = Instant.class)
public interface OrderMapper {

    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "totalAmount", source = "invoice.totalAmount")
    OrderDto toOrderDto(OrderEntity entity);

    @Mapping(target = "productPrice", source = "price")
    OrderItemDto toOrderItemDto(OrderItemEntity entity);

    @Mapping(target = "status", constant = "CREATE")
    @Mapping(target = "orderDate", expression = "java(Instant.now())")
    @Mapping(target = "orderItems", source = "items")
    OrderEntity createOrder(CreateOrderDto dto);

    @Mapping(target = "invoice.invoiceNumber", source = "invoice.number")
    @Mapping(target = "invoice.invoiceDate", expression = "java(Instant.now())")
    @Mapping(target = "orderItems", source = "items")
    OrderEntity updateOrderEntity(@MappingTarget OrderEntity entity, UpdateOrderDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @InheritConfiguration(name = "updateOrderEntity")
    OrderEntity patchOrderEntity(@MappingTarget OrderEntity entity, UpdateOrderDto dto);

    default OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }
}
