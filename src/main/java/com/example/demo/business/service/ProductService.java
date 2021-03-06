package com.example.demo.business.service;

import com.example.demo.business.model.Product;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ProductService {

    List<? extends Product> search(Map<String, String> requestParams);

    void resetData(InputStream inputStream);
}
