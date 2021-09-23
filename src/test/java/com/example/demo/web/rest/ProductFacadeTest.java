package com.example.demo.web.rest;


import com.example.demo.web.dto.ProductDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductFacadeTest extends RequestHelper {

    @Test
    public void getAll() throws Exception {
        List<ProductDTO> list = search(Collections.emptyMap());
        Assertions.assertThat(list).hasSize(100);
        Assertions.assertThat(list).allMatch(p -> "phone".equals(p.getType()) || "subscription".equals(p.getType()));
        Assertions.assertThat(list).allMatch(p -> p.getProperties().startsWith("color:") || p.getProperties().startsWith("gb_limit:"));
        Assertions.assertThat(list).allMatch(p -> p.getPrice() != null);
        Assertions.assertThat(list).allMatch(p -> p.getStoreAddress() != null);
    }

    @Test
    public void getAllWithParam() throws Exception {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("type", "phone");
        requestParams.put("min_price", "100");
        requestParams.put("max_price", "500");
        requestParams.put("city", "Stockholm");
        requestParams.put("property:color", "grön");
        List<ProductDTO> list = search(requestParams);
        Assertions.assertThat(list).hasSize(1);
        ProductDTO productDTO = list.get(0);
        Assertions.assertThat(productDTO.getType()).isEqualTo("phone");
        Assertions.assertThat(productDTO.getPrice()).isBetween(100F, 500F);
        Assertions.assertThat(productDTO.getStoreAddress()).endsWith("Stockholm");
        Assertions.assertThat(productDTO.getProperties()).isEqualTo("color:grön");
    }

    @Test
    public void postCSVFile() throws Exception {
        uploadDocument("/test_data.csv", "/product");
        List<ProductDTO> list = search(Collections.emptyMap());
        Assertions.assertThat(list).hasSize(3);

        uploadDocument("/data.csv", "/product");
        list = search(Collections.emptyMap());
        Assertions.assertThat(list).hasSize(100);
    }


}