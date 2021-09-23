package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional(rollbackFor = Exception.class)
public class AbstractTest {

}
