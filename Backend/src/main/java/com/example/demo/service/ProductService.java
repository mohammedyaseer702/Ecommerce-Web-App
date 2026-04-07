package com.example.demo.service;

import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // ================= ADD PRODUCT =================

    public ProductResponseDTO addProduct(ProductRequestDTO request) {

        Product product = mapToEntity(request);

        Product saved = productRepository.save(product);

        return mapToDTO(saved);
    }

    // ================= GET ACTIVE PRODUCTS =================

    public Page<ProductResponseDTO> getFilteredProducts(
        int page,
        int size,
        String sortBy,
        String direction,
        String category,
        Double minPrice,
        Double maxPrice) {

    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Product> productPage =
            productRepository.filterProducts(
                    category,
                    minPrice,
                    maxPrice,
                    pageable
            );

    return productPage.map(this::mapToDTO);
}


    // ================= UPDATE PRODUCT =================

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setActive(request.getActive());

        Product updated = productRepository.save(product);

        return mapToDTO(updated);
    }

    // ================= SOFT DELETE =================

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        product.setActive(false);
        productRepository.save(product);
    }

    // ================= MAPPING METHODS =================

    private ProductResponseDTO mapToDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .active(product.isActive())
                .build();
    }

    private Product mapToEntity(ProductRequestDTO request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .active(request.getActive())
                .build();
    }
}
