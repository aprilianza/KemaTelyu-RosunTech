package com.kematelyu.kematelyu.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  // tambahin ini
class HelloControllerIntegrationTest {

    @Autowired private MockMvc mvc;

    @Test
    void root_endpoint_is_reachable() throws Exception {
        mvc.perform(get("/"))
           .andExpect(status().isOk())
           .andExpect(content().string("Spring Boot Backend is Running!"));
    }
}
