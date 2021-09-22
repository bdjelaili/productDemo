package com.example.demo.business.service.impl;

import com.example.demo.business.Constant;
import com.example.demo.business.model.Phone;
import com.example.demo.business.model.Product;
import com.example.demo.business.model.Subscription;
import com.example.demo.business.repository.CustomPhoneRepository;
import com.example.demo.business.repository.CustomSubscriptionRepository;
import com.example.demo.business.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CustomPhoneRepository customPhoneRepository;

    @Autowired
    private CustomSubscriptionRepository customSubscriptionRepository;

    @Autowired
    protected EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<? extends Product> searchProduct(Map<String, String> requestParams) {
        String type = requestParams.get(Constant.type);
        if (type == null) {
            List<Product> result = new ArrayList<>();
            result.addAll(customPhoneRepository.search(requestParams));
            result.addAll(customSubscriptionRepository.search(requestParams));
            return result;
        }
        switch (type) {
            case Constant.phone:
                return customPhoneRepository.search(requestParams);
            case Constant.subscription:
                return customSubscriptionRepository.search(requestParams);
            default:
                throw new IllegalArgumentException("type must be 'phone' or 'subscription'");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initDb() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/data.csv")) {
            Scanner scanner = new Scanner(Objects.requireNonNull(is), StandardCharsets.UTF_8);
            //skip first line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                Product product = createProductInstance(scanner.nextLine());
                entityManager.persist(product);
                log.info("insert {}", product);
            }
        }
    }

    static Product createProductInstance(String line) {
        String[] split = line.split(",");
        if (split.length != 5) {
            throw new IllegalStateException("Bad line format : " + line);
        }
        Product product = initProductInstance(split);
        product.setPrice(Float.parseFloat(split[2]));
        product.setStoreAddress(split[3].substring(1));
        product.setCity(split[4].substring(1, split[4].length() - 1));
        return product;
    }

    static private Product initProductInstance(String[] split) {
        String type = split[0];
        switch (type) {
            case Constant.phone:
                Phone phone = new Phone();
                String colorProperty = split[1];
                String[] colorPropertySplit = colorProperty.split(":");
                if (colorPropertySplit.length != 2) {
                    throw new IllegalStateException("Bad colorProperty format : " + colorProperty);
                }
                phone.setColor(colorPropertySplit[1]);
                return phone;
            case Constant.subscription:
                Subscription subscription = new Subscription();
                String gbLimitProperty = split[1];
                String[] gbLimitPropertySplit = gbLimitProperty.split(":");
                if (gbLimitPropertySplit.length != 2) {
                    throw new IllegalStateException("Bad gbLimitProperty format : " + gbLimitProperty);
                }
                subscription.setGbLimit(Long.parseLong(gbLimitPropertySplit[1]));
                return subscription;
            default:
                throw new IllegalStateException("Unknown type : " + type);
        }
    }
}
