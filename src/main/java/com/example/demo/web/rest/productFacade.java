package com.example.demo.web.rest;

import com.example.demo.business.model.Phone;
import com.example.demo.business.model.Product;
import com.example.demo.business.model.Subscription;
import com.example.demo.business.service.ProductService;
import com.example.demo.web.dto.DataDTO;
import com.example.demo.web.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping(value = "/product")
@RestController
public class productFacade {


    @Autowired
    ProductService productService;

    @GetMapping(produces = "application/json;charset=UTF-8")
    public DataDTO getAll(@RequestParam Map<String, String> requestParams) {
        List<ProductDTO> dtos = productService.searchProduct(requestParams).stream().map(this::map).collect(Collectors.toList());
        return new DataDTO(dtos);
    }

    private ProductDTO map(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPrice(product.getPrice());
        productDTO.setStoreAddress(product.getStoreAddress() + ", " + product.getCity());

        if (product instanceof Phone) {
            productDTO.setType("phone");
            productDTO.setProperties("color:" + ((Phone) product).getColor());
        }
        if (product instanceof Subscription) {
            productDTO.setType("subscription");
            productDTO.setProperties("gb_limit:" + ((Subscription) product).getGbLimit());
        }
        return productDTO;
    }
}