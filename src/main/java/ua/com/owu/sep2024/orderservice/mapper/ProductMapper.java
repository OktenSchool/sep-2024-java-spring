package ua.com.owu.sep2024.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ua.com.owu.sep2024.orderservice.dto.product.CreateProductRequestDto;
import ua.com.owu.sep2024.orderservice.dto.product.ProductResponseDto;
import ua.com.owu.sep2024.orderservice.dto.product.UpdateProductRequestDto;
import ua.com.owu.sep2024.orderservice.model.Product;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ProductMapper {

    Product toProduct(CreateProductRequestDto requestDto);

    ProductResponseDto toResponseDto(Product product);

    Product updateProduct(@MappingTarget Product product, UpdateProductRequestDto requestDto);
}
