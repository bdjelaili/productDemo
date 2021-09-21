package com.example.demo.web.rest;


import com.example.demo.web.dto.DataDTO;
import com.example.demo.web.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class productFacadeTest {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @PostConstruct
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

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

    private List<ProductDTO> search(Map<String, String> requestParams) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/product");
        requestParams.forEach(requestBuilder::param);
        String str = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return context.getBean(ObjectMapper.class).readValue(str, DataDTO.class).getData();
    }
}