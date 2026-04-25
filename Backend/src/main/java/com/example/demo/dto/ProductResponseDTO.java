package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String category;
    private String imageUrl;
    private Boolean active;


    
}
