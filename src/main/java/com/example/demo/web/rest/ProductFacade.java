package com.example.demo.web.rest;

import com.example.demo.business.service.ProductService;
import com.example.demo.web.dto.DataDTO;
import com.example.demo.web.dto.ProductDTO;
import com.example.demo.web.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping(value = "/product")
@RestController
public class ProductFacade {


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Operation(summary = "search product by parameters : type, min_price, max_price, city, property:color, property:gb_limit_min and property:gb_limit_max")
    @GetMapping(produces = "application/json;charset=UTF-8")
    public DataDTO getAll(@RequestParam Map<String, String> requestParams) {
        List<ProductDTO> dtos = productService.search(requestParams)
                .stream()
                .map(p -> productMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
        return new DataDTO(dtos);
    }

    @PostMapping
    @Operation(summary = "Install data set")
    public void importData(@RequestParam("file") MultipartFile multipartFile)
            throws IOException {
        try (InputStream inputStream = new BufferedInputStream(multipartFile.getInputStream())) {
            productService.resetData(inputStream);
        }
    }
}