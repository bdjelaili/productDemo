package com.example.demo.business.repository;

import com.example.demo.business.model.Subscription;

import java.util.List;
import java.util.Map;

public interface CustomSubscriptionRepository {
    List<Subscription> search(Map<String, String> requestParams);
}
