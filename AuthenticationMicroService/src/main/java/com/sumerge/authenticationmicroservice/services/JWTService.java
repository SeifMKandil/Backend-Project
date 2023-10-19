package com.sumerge.authenticationmicroservice.services;

//this class is responsible for intercepting and manipulating
// the JWTToken for extracting data from it

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private static  final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";


    public String extractEmail(String token){

        return extractClaim(token , Claims::getSubject);
    }

    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //For generating a token without extraClaims
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>() , userDetails);
    }
    public String generateToken(Map<String , Object> extraClaims , UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        final  String userName = extractEmail(token);
        return userDetails.getUsername().equals(userName) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }

    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder() // Start Parsing the Token
                .setSigningKey(getSignInKey()) // Pass the SignInKey (Signature) to Parse , to ensure the sender is who is and to ensure that the data is not changed or manipulated
                .build() // Each Builder needs a Build method
                .parseClaimsJws(token) // Parsing the Claims (Data)
                .getBody(); // get the Data
    }

    private Key getSignInKey() {
        byte[] keyInBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyInBytes);
        //hmacKeyFor is the algorithm we will use
    }
}

// Notes:-
// Jwt token consists of three parts
// Header , Payload , Signature
//399E8C31FAD77EEA6A332D19CC684
// Payload contains the Claims (Data) in this case it will be the user data in addition to some extra data