package com.example.demo.controller;

import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import com.example.demo.service.CloudinaryService;
import com.example.demo.repository.ProductRepository;
import com.example.demo.entity.Product;



@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController {

    private final ProductService productService;
    private final CloudinaryService cloudinaryService;
    private final ProductRepository productRepository;

    // ================= PUBLIC: GET PRODUCTS =================

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
                        page, size, sortBy, direction,
                        category, minPrice, maxPrice
                );

        return ResponseEntity.ok(
                ApiResponse.success("Products fetched successfully", products)
        );
    }



    @GetMapping("/public/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ================= PUBLIC: SEARCH =================

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<ProductResponseDTO> products =
                productService.searchProducts(keyword, page, size);

        return ResponseEntity.ok(
                ApiResponse.success("Search results", products)
        );
    }


    @PostMapping("/admin/upload")
public ResponseEntity<ApiResponse<String>> uploadImage(
        @RequestParam("file") MultipartFile file) {

    String url = cloudinaryService.uploadFile(file);

    return ResponseEntity.ok(
            ApiResponse.<String>builder()
                    .success(true)
                    .message("Uploaded")
                    .data(url)
                    .build()
    );
}
    // ================= PUBLIC: GET SINGLE PRODUCT =================

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductById(
            @PathVariable Long id
    ) {

        ProductResponseDTO product =
                productService.getProductById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Product fetched", product)
        );
    }

    // ================= ADMIN: GET ALL (INCLUDING INACTIVE) =================

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getAllProductsAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<ProductResponseDTO> products =
                productService.getAllProductsAdmin(page, size);

        return ResponseEntity.ok(
                ApiResponse.success("All products (admin)", products)
        );
    }

    // ================= ADMIN: ADD PRODUCT =================

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> addProduct(
            @Valid @RequestBody ProductRequestDTO request) {

        ProductResponseDTO savedProduct =
                productService.addProduct(request);

        return ResponseEntity.ok(
                ApiResponse.success("Product created successfully", savedProduct)
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
                ApiResponse.success("Product updated successfully", updatedProduct)
        );
    }

    

    // ================= ADMIN: DELETE PRODUCT =================

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                ApiResponse.success("Product deleted successfully")
        );
    }



    
}