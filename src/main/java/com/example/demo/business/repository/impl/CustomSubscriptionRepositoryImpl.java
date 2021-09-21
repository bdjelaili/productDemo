package com.example.demo.business.repository.impl;

import com.example.demo.business.Constant;
import com.example.demo.business.model.Subscription;
import com.example.demo.business.repository.CustomSubscriptionRepository;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
class CustomSubscriptionRepositoryImpl extends AbstractProductRepositoryImpl<Subscription> implements CustomSubscriptionRepository {

    @Override
    List<Predicate> getCustomPredicates(CriteriaBuilder cb, Root<Subscription> root, Map<String, String> requestParams) {
        List<Predicate> predicates = new ArrayList<>();
        Optional.ofNullable(requestParams.get(Constant.gbLimitMinProperty)).ifPresent(v -> predicates.add(cb.greaterThanOrEqualTo(root.get("gbLimit"), v)));
        Optional.ofNullable(requestParams.get(Constant.gbLimitMaxProperty)).ifPresent(v -> predicates.add(cb.lessThanOrEqualTo(root.get("gbLimit"), v)));
        return predicates;
    }

    @Override
    Class<Subscription> getTypeClass() {
        return Subscription.class;
    }
}
