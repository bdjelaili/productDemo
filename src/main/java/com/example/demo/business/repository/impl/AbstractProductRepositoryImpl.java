package com.example.demo.business.repository.impl;

import com.example.demo.business.Constant;
import com.example.demo.business.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

abstract class AbstractProductRepositoryImpl<T extends Product> {

    @Autowired
    protected EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<T> search(Map<String, String> requestParams) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(getTypeClass()).distinct(true);
        Root<T> root = query.from(getTypeClass());


        List<Predicate> predicates = new ArrayList<>();
        Optional.ofNullable(requestParams.get(Constant.minPrice)).ifPresent(v -> predicates.add(cb.greaterThanOrEqualTo(root.get("price"), v)));
        Optional.ofNullable(requestParams.get(Constant.maxPrice)).ifPresent(v -> predicates.add(cb.lessThanOrEqualTo(root.get("price"), v)));
        Optional.ofNullable(requestParams.get(Constant.city)).ifPresent(v -> predicates.add(cb.equal(root.get("city"), v)));

        predicates.addAll(getCustomPredicates(cb, root, requestParams));

        Predicate predicate = cb.and(predicates.toArray(new Predicate[0]));
        TypedQuery<T> typedQuery = entityManager.createQuery(query.where(predicate));

        return typedQuery.getResultList();
    }

    abstract List<Predicate> getCustomPredicates(CriteriaBuilder cb, Root<T> root, Map<String, String> requestParams);

    abstract Class<T> getTypeClass();
}
