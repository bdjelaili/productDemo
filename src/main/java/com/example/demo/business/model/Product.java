package com.example.demo.business.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "price must be positive")
    @Column(name = "price", nullable = false)
    private Float price;

    @NotBlank(message = "address cannot not be empty")
    @Column(name = "store_address", nullable = false)
    private String storeAddress;

    @NotBlank(message = "city cannot not be empty")
    @Column(name = "city", nullable = false)
    private String city;
}
