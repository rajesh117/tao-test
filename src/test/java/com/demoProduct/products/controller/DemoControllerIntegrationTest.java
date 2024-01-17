package com.demoProduct.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demoproduct.products.entity.ProductDTO;
import com.demoproduct.products.entity.ProductEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateProducts() throws Exception {

        ProductDTO productDTO = new ProductDTO("Bag", 1000, 1);
        String requestJson = objectMapper.writeValueAsString(productDTO);

        // Perform the POST request
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // Verify the response is 201 Created
        result.andExpect(status().isCreated());
    }

    @Test
    public void createProduct_PriceExceedsLimit() throws Exception {
        // Create a product with a price exceeding $10,000
        ProductDTO productDTO = new ProductDTO("Bag", 11000, 1);
        String requestJson = objectMapper.writeValueAsString(productDTO);

        // Perform the POST request
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // Verify the response is 406 Bad Request
        result.andExpect(status().isNotAcceptable());
    }

    @Test
    public void createProduct_PriceExceedsApprovalQueue() throws Exception {
        // Create a product with a price exceeding $5,000
        ProductDTO productDTO = new ProductDTO("tv", 6000, 1);
        String requestJson = objectMapper.writeValueAsString(productDTO);

        // Perform the POST request
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // Verify the response is 201 Created, as it should be pushed to the approval queue.
        result.andExpect(status().isCreated());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productName").value("Bag"))
                .andExpect(jsonPath("$[1].productName").value("tv"));
    }
}
