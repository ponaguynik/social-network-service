package com.testtask.socialnetworkservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testtask.socialnetworkservice.SocialNetworkServiceApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkServiceApplication.class)
@AutoConfigureMockMvc(secure = false)
@TestPropertySource("classpath:application-integrationtest.properties")
@Transactional
public abstract class AbstractControllerIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    protected final ObjectMapper objectMapper = new ObjectMapper();
}
