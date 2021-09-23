package com.example.demo.web.rest;

import com.example.demo.AbstractTest;
import com.example.demo.web.dto.DataDTO;
import com.example.demo.web.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class RequestHelper extends AbstractTest {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @PostConstruct
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    protected List<ProductDTO> search(Map<String, String> requestParams) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/product");
        requestParams.forEach(requestBuilder::param);
        String str = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return context.getBean(ObjectMapper.class).readValue(str, DataDTO.class).getData();
    }

    protected void uploadDocument(String file, String url) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(url).file(fileAsPart(file)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    protected static MockMultipartFile fileAsPart(String fileName) throws IOException {
        try (InputStream inputStream = RequestHelper.class.getResourceAsStream(fileName)) {
            Assertions.assertThat(inputStream).isNotNull();
            return new MockMultipartFile(
                    "file",
                    Paths.get(fileName).getFileName().toString(),
                    "multipart/form-data",
                    inputStream);
        }
    }
}
