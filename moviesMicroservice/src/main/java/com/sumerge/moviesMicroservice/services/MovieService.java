package com.sumerge.moviesMicroservice.services;

import com.sumerge.moviesMicroservice.exception.TokenValidationException;
import com.sumerge.moviesMicroservice.models.Movie;

import com.sumerge.moviesMicroservice.services.repository.MovieRepo;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieService {

    private MovieRepo repo;

    private JwtService service;

    public MovieService(MovieRepo repo, JwtService service) {
        this.repo = repo;
        this.service = service;
    }

    public List<Movie> getMovies(HttpServletRequest request , int pageNumber) {
            String token = getToken(request);
            System.out.println(token);
            if (service.validateToken(token)) {
                Pageable paging = PageRequest.of(
                        pageNumber, 5, Sort.by("rating").ascending());
                Page<Movie> page = repo.findAll(paging);
                return page.getContent();
            }

        throw new TokenValidationException("Token must me passed in the auth header");
    }


    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return token;
        }
        return null;
    }
}
