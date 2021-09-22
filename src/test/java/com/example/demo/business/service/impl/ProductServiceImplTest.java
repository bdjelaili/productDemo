package com.example.demo.business.service.impl;


import com.example.demo.business.model.Phone;
import com.example.demo.business.model.Product;
import com.example.demo.business.model.Subscription;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceImplTest {

    @Test
    public void createProduct() {
        Product phone = ProductServiceImpl.createProductInstance("phone,color:grön,277.00,\"Blake gränden, Karlskrona\"");
        assertThat(phone).isInstanceOf(Phone.class);
        assertThat(phone.getStoreAddress()).isEqualTo("Blake gränden");
        assertThat(phone.getCity()).isEqualTo("Karlskrona");
        assertThat(phone.getPrice()).isEqualTo(277F);
        assertThat(((Phone) phone).getColor()).isEqualTo("grön");


        Product subscription = ProductServiceImpl.createProductInstance("subscription,gb_limit:50,415.00,\"Odell gatan, Stockholm\"");
        assertThat(subscription).isInstanceOf(Subscription.class);
        assertThat(subscription.getStoreAddress()).isEqualTo("Odell gatan");
        assertThat(subscription.getCity()).isEqualTo("Stockholm");
        assertThat(subscription.getPrice()).isEqualTo(415F);
        assertThat(((Subscription) subscription).getGbLimit()).isEqualTo(50L);
    }

    @Test
    public void createProductBadLineFormat() {
        assertErrorMessage("aa,bb", "Bad line format : aa,bb");
        assertErrorMessage("badType,color:grön,277.00,\"Blake gränden, Karlskrona\"", "Unknown type : badType");
        assertErrorMessage("phone,color::grön,277.00,\"Blake gränden, Karlskrona\"", "Bad colorProperty format : color::grön");
        assertErrorMessage("subscription,gb_limit::50,415.00,\"Odell gatan, Stockholm\"", "Bad gbLimitProperty format : gb_limit::50");
    }

    private static void assertErrorMessage(String line, String expectedMsg) {
        try {
            ProductServiceImpl.createProductInstance(line);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(expectedMsg);
            return;
        }
        throw new IllegalStateException("missing exception");
    }
}