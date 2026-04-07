package com.example.demo.controller;

import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    // ================= PUBLIC PRODUCTS =================


@GetMapping("/public")
public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getProducts(

        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,

        @RequestParam(required = false) String category,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice
) {

    Page<ProductResponseDTO> products =
            productService.getFilteredProducts(
                    page,
                    size,
                    sortBy,
                    direction,
                    category,
                    minPrice,
                    maxPrice
            );

    return ResponseEntity.ok(
            ApiResponse.<Page<ProductResponseDTO>>builder()
                    .success(true)
                    .message("Products fetched successfully")
                    .data(products)
                    .timestamp(LocalDateTime.now())
                    .build()
    );
}


    // ================= ADMIN: ADD PRODUCT =================

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> addProduct(
            @Valid @RequestBody ProductRequestDTO request) {

        ProductResponseDTO savedProduct =
                productService.addProduct(request);

        return ResponseEntity.ok(
                ApiResponse.<ProductResponseDTO>builder()
                        .success(true)
                        .message("Product created successfully")
                        .data(savedProduct)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ================= ADMIN: UPDATE PRODUCT =================

    @PutMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO request) {

        ProductResponseDTO updatedProduct =
                productService.updateProduct(id, request);

        return ResponseEntity.ok(
                ApiResponse.<ProductResponseDTO>builder()
                        .success(true)
                        .message("Product updated successfully")
                        .data(updatedProduct)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ================= ADMIN: DELETE PRODUCT =================

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Product deleted successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
