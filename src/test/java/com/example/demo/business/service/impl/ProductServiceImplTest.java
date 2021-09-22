package com.example.demo.business.service.impl;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductServiceImplTest {

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
            Assertions.assertThat(e.getMessage()).isEqualTo(expectedMsg);
            return;
        }
        throw new IllegalStateException("missing exception");
    }
}