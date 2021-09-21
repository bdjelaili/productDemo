package com.example.demo.business.repository.impl;

import com.example.demo.business.Constant;
import com.example.demo.business.model.Phone;
import com.example.demo.business.repository.CustomPhoneRepository;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
class CustomPhoneRepositoryImpl extends AbstractProductRepositoryImpl<Phone> implements CustomPhoneRepository {

    @Override
    List<Predicate> getCustomPredicates(CriteriaBuilder cb, Root<Phone> root, Map<String, String> requestParams) {
        List<Predicate> predicates = new ArrayList<>();
        Optional.ofNullable(requestParams.get(Constant.colorProperty)).ifPresent(v -> predicates.add(cb.equal(root.get("color"), v)));
        return predicates;
    }

    @Override
    Class<Phone> getTypeClass() {
        return Phone.class;
    }
}
