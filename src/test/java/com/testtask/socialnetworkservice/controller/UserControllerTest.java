package com.testtask.socialnetworkservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void loadShouldReturnOkOnValidUrl() throws Exception {
        String url = "https://jsonplaceholder.typicode.com/users";
        mockMvc.perform(post("/users/load")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.createObjectNode().put("url", url).toString()))
                .andExpect(status().isOk());
    }

}