package com.sumerge.authenticationmicroservice.SecurityTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("authenticate is a public endpoint should return status 200 if user enters valid email and valid password ")
    public void publicAuthenticate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Seif@gmail.com\", \"password\":\"qweasd\" }"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("authenticate is a public endpoint should return a token if user enters valid email and valid password ")
    public void publicAuthenticateToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Seif@gmail.com\", \"password\":\"qweasd\" }"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("authenticate endpoint should return 403 forbidden and return no token if user enters a valid email and a invalid password")
    public void authenticateInvalidPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Seif@gmail.com\", \"password\":\"qweasddd\" }"))
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("authenticate endpoint should return 403 forbidden and return no token if user enters a Invalid email and a valid password")
    public void authenticateInvalidEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Seif123@gmail.com\", \"password\":\"qweasd\" }"))
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("authenticate endpoint should return 403 forbidden and return no token if user enters a Invalid email and a Invalid password")
    public void authenticateInvalidEmailPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Seif123@gmail.com\", \"password\":\"qweasddr\" }"))
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
