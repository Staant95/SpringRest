package com.smartshop.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshop.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private final String loginUrl = "/auth/login";

    @Test
    public void testLoginWithoutCredentials() throws Exception {

        this.mockMvc.perform(
                post(loginUrl)
                .contentType("application/json")
        ).andExpect(status().is(400));
    }

    @Test
    public void testLoginWithBadCredentials() throws Exception {

        User user = new User();
        user.setEmail("stas@gmail.com");
        user.setPassword("secretxxx");

        this.mockMvc.perform(
                post(this.loginUrl)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user))
        ).andExpect(status().is(400));
    }

    @Test
    public void testLoginWithValidCredentials() throws Exception {
        User user = new User();
        user.setEmail("stas@gmail.com");
        user.setPassword("secret");

        this.mockMvc.perform(
                post(this.loginUrl)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user))
        ).andExpect(status().is(200));
    }

}
