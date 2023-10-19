package com.sumerge.moviesMicroservice;

import com.sumerge.moviesMicroservice.exception.TokenValidationException;
import com.sumerge.moviesMicroservice.services.JwtService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtServiceTest {
    private String validAuthToken="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIb3NzYW1AZ21haWwuY29tIiwiaWF0IjoxNjk3NjgxNTM2LCJleHAiOjE2OTc2ODI5NzZ9.2Z5eKdu9xQguj1Pbn7UqrI6uP45vp516gUFtlI2EDK0";

    @Test
    public void validToken() {
        JwtService jwtService = new JwtService();
        String validToken = validAuthToken;
        boolean isValid = jwtService.validateToken(validToken);
        assertTrue(isValid);

    }



    @Test
    @DisplayName("Invalid Token with invalid header")
    public void inValidHeaderToken() {
        JwtService jwtService = new JwtService();
        String validToken = "eyJhbBHciOiJIUzI1NiJ9.eyJzdWIiOiJIb3NzYW1AZ21haWwuY29tIiwiaWF0IjoxNjk3NjgxNTM2LCJleHAiOjE2OTc2ODI5NzZ9.2Z5eKdu9xQguj1Pbn7UqrI6uP45vp516gUFtlI2EDK0";
        assertThrows(TokenValidationException.class, () -> {
            jwtService.validateToken(validToken);
        });
    }

    @Test
    @DisplayName("Invalid Token with invalid claims")
    public void inValidClaimsToken() {
        JwtService jwtService = new JwtService();
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIb3NzYW1AZ21haWwuY27tIiwiaWF0IjoxNjk3NjgxNTM2LCJleHAiOjE2OTc2ODI5NzZ9.2Z5eKdu9xQguj1Pbn7UqrI6uP45vp516gUFtlI2EDK0";
        assertThrows(TokenValidationException.class, () -> {
            jwtService.validateToken(validToken);
        });
    }


    @Test
    @DisplayName("Invalid Token with invalid signature")
    public void inValidSignatureToken() {
        JwtService jwtService = new JwtService();
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIb3NzYW1AZ21haWwuY29tIiwiaWF0IjoxNjk3NjgxNTM2LCJleHAiOjE2OTc2ODI5NzZ9.2Z5eKdu9xQguj1Pbn7UqrI6uP45vp516gUFtlI2ECLo";
        assertThrows(TokenValidationException.class, () -> {
            jwtService.validateToken(validToken);
        });
    }

    @Test
    @DisplayName("No token was sent or token is empty")
    public void invalidEmptyToken() {
        JwtService jwtService = new JwtService();
        String validToken = "";
        assertThrows(TokenValidationException.class, () -> {
            jwtService.validateToken(validToken);
        });
    }
}
