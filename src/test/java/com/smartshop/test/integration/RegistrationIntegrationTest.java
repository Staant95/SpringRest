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
public class RegistrationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    private final String registrationUrl = "/auth/register";

    @Test
    public void registerUserWithValidInput() throws Exception {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("secret");
        user.setName("Integration");
        user.setLastname("Test");

        this.mockMvc.perform(
                post(this.registrationUrl)
                .contentType("application/json")
                .content(mapper.writeValueAsString(user))
        ).andExpect(status().is(201));

    }


    @Test
    public void registerUserWithInvalidInput() throws Exception {
        User user = new User();

        this.mockMvc.perform(
                post(this.registrationUrl)
                .contentType("application/json")
                .content(mapper.writeValueAsString(user))
        ).andExpect(status().is(422));
    }

    @Test
    public void registerUserWithDuplicateEmail() throws Exception {
        User user = new User();
        user.setEmail("stas@gmail.com");
        user.setPassword("secret");
        user.setName("Integration");
        user.setLastname("Test");

        this.mockMvc.perform(
                post(this.registrationUrl)
                .contentType("application/json")
                .content(mapper.writeValueAsString(user))
        ).andExpect(status().is(409));
    }

}
