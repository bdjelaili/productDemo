package com.example.demo.business.repository;

import com.example.demo.business.model.Phone;

import java.util.List;
import java.util.Map;

public interface CustomPhoneRepository {
    List<Phone> search(Map<String, String> requestParams);
}
