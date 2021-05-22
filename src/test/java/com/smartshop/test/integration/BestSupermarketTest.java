package com.smartshop.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshop.models.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BestSupermarketTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Value("${token}")
    private String token;

    private final Position target = new Position(42.405561,12.872578 );

    @Test
    public void testBestSupermarketWithoutQueryParams() throws Exception {

        this.mockMvc.perform(
                post("/shoplists/25/supermarkets")
                        .header("Authorization", "Bearer " + this.token)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(this.target))
        )
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testBestSupermarketWithRangeQueryParam() throws Exception {

        this.mockMvc.perform(
                post("/shoplists/25/supermarkets")
                        .header("Authorization", "Bearer " + this.token)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(this.target))
                        .queryParam("range", "1")
        )
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.length()").value(1));
    }
}
