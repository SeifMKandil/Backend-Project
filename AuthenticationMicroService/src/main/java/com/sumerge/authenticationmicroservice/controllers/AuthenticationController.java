package com.sumerge.authenticationmicroservice.controllers;

import com.sumerge.authenticationmicroservice.auth.AuthenticationRequest;
import com.sumerge.authenticationmicroservice.auth.AuthenticationResponse;
import com.sumerge.authenticationmicroservice.auth.RegisterRequest;
import com.sumerge.authenticationmicroservice.services.AuthenticationService;

import com.sumerge.authenticationmicroservice.services.RecaptchaService;
import com.sun.jdi.PrimitiveValue;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {
    private final AuthenticationService service;
    private final RecaptchaService captchaService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request

    ){

        String captchaVerifyMessage =
                captchaService.verifyRecaptcha( request.getCaptcha());

        if ( StringUtils.isNotEmpty(captchaVerifyMessage)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", captchaVerifyMessage);
            return null;

        }
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
