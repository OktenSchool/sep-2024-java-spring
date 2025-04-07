package ua.com.owu.sep2024.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.owu.sep2024.orderservice.dto.MailDto;
import ua.com.owu.sep2024.orderservice.dto.product.CreateProductRequestDto;
import ua.com.owu.sep2024.orderservice.dto.product.ProductResponseDto;
import ua.com.owu.sep2024.orderservice.dto.product.UpdateProductRequestDto;
import ua.com.owu.sep2024.orderservice.mapper.ProductMapper;
import ua.com.owu.sep2024.orderservice.model.Product;
import ua.com.owu.sep2024.orderservice.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final MailService mailService;

    @Transactional
    public ProductResponseDto createProduct(CreateProductRequestDto requestDto) {
        var product = productMapper.toProduct(requestDto);
        var saved = productRepository.save(product);
        ProductResponseDto responseDto = productMapper.toResponseDto(saved);
        mailService.sendMail(MailDto.builder()
                .to(requestDto.createdBy())
                .title("Product Created")
                .message("Details: " + responseDto)
                .build());
        return responseDto;
    }

    public Optional<ProductResponseDto> updateProduct(String productId, UpdateProductRequestDto requestDto) {
        return productRepository.findById(productId)
                .map(product -> productMapper.updateProduct(product, requestDto))
                .map(productRepository::save)
                .map(productMapper::toResponseDto);
    }

    public List<ProductResponseDto> getAllProducts(BigDecimal minPrice) {
        List<Product> results;

        if (minPrice != null) {
            results = productRepository.findAllByMinPrice(minPrice);
        } else {
            results = productRepository.findAll();
        }

        return results.stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }
}
