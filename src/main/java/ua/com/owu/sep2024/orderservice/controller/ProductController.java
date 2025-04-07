package ua.com.owu.sep2024.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.owu.sep2024.orderservice.dto.product.CreateProductRequestDto;
import ua.com.owu.sep2024.orderservice.dto.product.ProductResponseDto;
import ua.com.owu.sep2024.orderservice.dto.product.UpdateProductRequestDto;
import ua.com.owu.sep2024.orderservice.model.Product;
import ua.com.owu.sep2024.orderservice.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<ProductResponseDto> getProducts(@RequestParam(name = "minPrice", required = false) BigDecimal minPrice) {
        return productService.getAllProducts(minPrice);
    }

    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody @Valid CreateProductRequestDto requestDto) {
        return productService.createProduct(requestDto);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable String id, @RequestBody @Valid UpdateProductRequestDto requestDto) {
        return ResponseEntity.of(productService.updateProduct(id, requestDto));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
