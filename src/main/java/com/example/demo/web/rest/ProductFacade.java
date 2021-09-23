package com.example.demo.web.rest;

import com.example.demo.business.service.ProductService;
import com.example.demo.web.dto.DataDTO;
import com.example.demo.web.dto.ProductDTO;
import com.example.demo.web.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping(value = "/product")
@RestController
public class ProductFacade {


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Operation(summary = "search product by parameters : type, min_price, max_price, city, property:color, property:gb_limit_min and property:gb_limit_max")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the result", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DataDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters values", content = @Content)})
    @GetMapping(produces = "application/json;charset=UTF-8")
    public DataDTO getAll(
            @Parameter(description = "Parameters used for search : <br>" +
                    "- type : The product type. (String. Can be 'phone' or 'subscription')<br>" +
                    "- min_price : The minimum price in SEK. (Number)<br>" +
                    "- max_price : The maximum price in SEK. (Number)<br>" +
                    "- city : The city in which a store is located. (String)<br>" +
                    "- property:color : The color of the phone. (String)<br>" +
                    "- property:gb_limit_min : The minimum GB limit of the subscription. (Number)<br>" +
                    "- property:gb_limit_max : The maximum GB limit of the subscription. (Number)<br>" +
                    "We can send those parameters as a map in the body or as a url like /product?type=subscription&max_price=1000&city=Stockholm")
            @RequestParam Map<String, String> requestParams) {
        List<ProductDTO> dtos = productService.search(requestParams)
                .stream()
                .map(p -> productMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
        return new DataDTO(dtos);
    }

    @PostMapping
    @Operation(summary = "Install CSV file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insertion successful", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Error file content", content = @Content)})
    public void importData(
            @Parameter(description = "Insert products from the CSV file <br>" +
                    "The CSV file must be respect this format :<br>" +
                    "Product type,Product properties,Price,Store address<br>" +
                    "phone,color:grön,277.00,\"Blake gränden, Karlskrona\"<br>" +
                    "subscription,gb_limit:50,415.00,\"Odell gatan, Stockholm\"<br>" +
                    "subscription,gb_limit:10,202.00,\"Fausto vägen, Karlskrona\"")
            @RequestParam("file") MultipartFile multipartFile)
            throws IOException {
        try (InputStream inputStream = new BufferedInputStream(multipartFile.getInputStream())) {
            productService.resetData(inputStream);
        }
    }
}