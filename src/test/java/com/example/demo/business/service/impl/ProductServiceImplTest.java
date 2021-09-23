package com.example.demo.business.service.impl;


import com.example.demo.business.model.Phone;
import com.example.demo.business.model.Product;
import com.example.demo.business.model.Subscription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void createProduct() {
        Product phone = productService.parseAndPersist("phone,color:grön,277.00,\"Blake gränden, Karlskrona\"");
        assertThat(phone.getId()).isNotNull();
        assertThat(phone).isInstanceOf(Phone.class);
        assertThat(phone.getStoreAddress()).isEqualTo("Blake gränden");
        assertThat(phone.getCity()).isEqualTo("Karlskrona");
        assertThat(phone.getPrice()).isEqualTo(277F);
        assertThat(((Phone) phone).getColor()).isEqualTo("grön");


        Product subscription = productService.parseAndPersist("subscription,gb_limit:50,415.00,\"Odell gatan, Stockholm\"");
        assertThat(subscription).isInstanceOf(Subscription.class);
        assertThat(subscription.getStoreAddress()).isEqualTo("Odell gatan");
        assertThat(subscription.getCity()).isEqualTo("Stockholm");
        assertThat(subscription.getPrice()).isEqualTo(415F);
        assertThat(((Subscription) subscription).getGbLimit()).isEqualTo(50L);
    }

    @Test
    public void createProductWithNegativePrice() {
        assertPersistError("phone,color:grön,-277.00,\"Blake gränden, Karlskrona\"", "price must be positive");
    }

    @Test
    public void createProductWithEmptyStoreAddress() {
        assertPersistError("phone,color:grön,277.00,\" , Karlskrona\"", "address cannot not be empty");
    }

    @Test
    public void createProductWithEmptyCity() {
        assertPersistError("phone,color:grön,277.00,\"Blake gränden, \"", "city cannot not be empty");
    }

    @Test
    public void createPhoneProductWithEmptyColor() {
        assertPersistError("phone,color: ,277.00,\"Blake gränden, Stockholm\"", "color cannot not be empty");
    }

    @Test
    public void createSubscriptionProductWithNegativeGbLimit() {
        assertPersistError("subscription,gb_limit:-20,415.00,\"Odell gatan, Stockholm\"", "gbLimit must be positive");
    }

    @Test
    public void createProductBadLineFormat() {
        assertParseErrorMessage("aa,bb", "Bad line format : aa,bb");
        assertParseErrorMessage("badType,color:grön,277.00,\"Blake gränden, Karlskrona\"", "Unknown type : badType");
        assertParseErrorMessage("phone,color::grön,277.00,\"Blake gränden, Karlskrona\"", "Bad colorProperty format : color::grön");
        assertParseErrorMessage("subscription,gb_limit::50,415.00,\"Odell gatan, Stockholm\"", "Bad gbLimitProperty format : gb_limit::50");
    }

    private static void assertParseErrorMessage(String line, String expectedMsg) {
        try {
            ProductServiceImpl.parse(line);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(expectedMsg);
            return;
        }
        throw new IllegalStateException("missing exception");
    }

    private void assertPersistError(String line, String... expectedMsg) {
        try {
            productService.parseAndPersist(line);
        } catch (ConstraintViolationException e) {
            Set<String> errors = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
            assertThat(errors).containsExactly(expectedMsg);
            return;
        }
        throw new IllegalStateException("missing exception");
    }
}