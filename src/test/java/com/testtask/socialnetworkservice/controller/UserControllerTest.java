package com.testtask.socialnetworkservice.controller;

import com.testtask.socialnetworkservice.dto.RequestUrl;
import com.testtask.socialnetworkservice.model.Address;
import com.testtask.socialnetworkservice.model.Geo;
import com.testtask.socialnetworkservice.model.User;
import com.testtask.socialnetworkservice.repository.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends AbstractControllerIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void loadShouldLoadUsersByUrlToDb() throws Exception {
        List<User> expectedUsers = Arrays.asList(
                User.builder().id(1L).name("Some name").phone("123-123").username("userName")
                        .address(Address.builder()
                                .geo(Geo.builder().lat(12.42).lng(123.12).build())
                                .build())
                        .build(),
                User.builder().id(2L).name("Some name 1").phone("123-123").username("userName1")
                        .address(Address.builder()
                                .geo(Geo.builder().lat(12.42).lng(123.12).build())
                                .build())
                        .build()
        );
        String url = "http://localhost:8080/users";
        doReturn(expectedUsers.toArray(new User[0])).when(restTemplate).getForObject(url, User[].class);

        mockMvc.perform(post("/users/load")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(new RequestUrl(url))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUsers)));

        assertEquals(expectedUsers, userRepository.findAll());
    }

}