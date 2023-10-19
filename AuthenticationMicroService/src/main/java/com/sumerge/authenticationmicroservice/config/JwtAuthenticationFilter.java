package com.sumerge.authenticationmicroservice.config;

import com.sumerge.authenticationmicroservice.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Extending this class to make sure the
// class JwtAuthenticationFilter is called everytime a user sends a request
@Component //to treat the class as Spring Bean
@RequiredArgsConstructor //this creates automatically a constructor to any final field (attribute) i add in the class
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal

            (@NonNull HttpServletRequest request, //the request sent by the user we want to intercept it to controll and manipulate the response
             @NonNull HttpServletResponse response, //response we will send back
             @NonNull FilterChain filterChain // return a List of filters that we will need
            ) throws ServletException, IOException {
        // Get teh header from the request
        // The header that contains the jwtToken is called Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        //The header must start with Bearer then the JwtToken
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        //JwtToken is found in the header after "Bearer " which is after 7 chars
        jwtToken = authHeader.substring(7);
        userEmail = jwtService.extractEmail(jwtToken);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);


            }
        }
        filterChain.doFilter(request, response);
    }
}
