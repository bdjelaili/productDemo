package com.example.demo.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDTO {
    private String type;
    private String properties;
    private Float price;
    @JsonProperty("store_address")
    private String storeAddress;
}
