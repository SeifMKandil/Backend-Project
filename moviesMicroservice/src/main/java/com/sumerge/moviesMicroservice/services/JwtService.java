package com.sumerge.moviesMicroservice.services;

import com.sumerge.moviesMicroservice.exception.TokenValidationException;
import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;
import java.security.Key;


@Service
public class JwtService {
    private static  final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new TokenValidationException("Token is not sent");
        }
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new TokenValidationException("Token is invalid: " + ex.getMessage());
        }
    }



    public Key getSignInKey() {
        byte[] keyInBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyInBytes);
    }
}
