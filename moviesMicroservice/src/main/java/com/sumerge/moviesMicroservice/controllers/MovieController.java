package com.sumerge.moviesMicroservice.controllers;

import com.sumerge.moviesMicroservice.exception.TokenValidationException;
import com.sumerge.moviesMicroservice.models.Movie;

import com.sumerge.moviesMicroservice.services.repository.MovieRepo;
import com.sumerge.moviesMicroservice.services.JwtService;

import com.sumerge.moviesMicroservice.services.MovieService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("movie")
@CrossOrigin(origins = "http://localhost:4200")
public class MovieController {
    @Autowired
    MovieRepo repo;
    @Autowired
    private JwtService service;
    @Autowired
    private MovieService movieService;

    @PostMapping("/addMovie")
    public void addMovies(@RequestBody List<Movie> movies, HttpServletRequest request) {
        String token = movieService.getToken(request);
        if (service.validateToken(token)) {
            repo.saveAll(movies);
        } else {
            throw new TokenValidationException("Unexpected Error occurred");
        }
    }

    @GetMapping("/{pageNumber}")
    public List<Movie> getMovies(@PathVariable int pageNumber, HttpServletRequest request) {
                return movieService.getMovies(request , pageNumber);
            }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = repo.findById(id);
        if (movie.isPresent()) {
            return ResponseEntity.ok(movie.get());
        } else {
            return ResponseEntity.notFound().build();
        }


    }

//    For Testing Purposes
//    @GetMapping("/token")
//    public boolean validateToken(HttpServletRequest request) {
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            if (service.validateToken(token)) {
//                System.out.println("The token is valid");
//                return true;
//            }
//        }
//        return false;
//    }


}