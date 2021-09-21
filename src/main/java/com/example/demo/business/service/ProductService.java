package com.example.demo.business.service;

import com.example.demo.business.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<? extends Product> searchProduct(Map<String, String> requestParams);
}
