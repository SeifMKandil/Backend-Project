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
public class RegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Register is a public endpoint should return status 200 if not authenticated")
    public void publicRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"SeikoTest\", \"lastName\": \"Kandil\", \"email\": \"Seeko@gmail.com\", \"password\":\"qweasd\" }"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Register endpoint should return a token if the body was sent correctly")
    public void registerToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"Seiko\", \"lastName\": \"Kandil\", \"email\": \"Seeko@gmail.com\", \"password\":\"qweasd\" }"))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testProtectedEndpointRequiresAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/demo-controller"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


}
