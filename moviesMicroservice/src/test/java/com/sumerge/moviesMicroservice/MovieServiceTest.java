package com.sumerge.moviesMicroservice;

import com.sumerge.moviesMicroservice.controllers.MovieController;
import com.sumerge.moviesMicroservice.exception.TokenValidationException;
import com.sumerge.moviesMicroservice.models.Movie;
import com.sumerge.moviesMicroservice.services.repository.MovieRepo;
import com.sumerge.moviesMicroservice.services.JwtService;
import com.sumerge.moviesMicroservice.services.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@AutoConfigureMockMvc
@SpringBootTest
public class MovieServiceTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieRepo movieRepo;

    @Autowired
    private MockMvc mockMvc;


    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private MovieService movieService;


    @BeforeEach
    public void setUp() {
        movieRepo = Mockito.mock(MovieRepo.class);
        movieController =Mockito.mock(MovieController.class);
        jwtService = Mockito.mock(JwtService.class);
        request = Mockito.mock(HttpServletRequest.class);
        movieService = new MovieService(movieRepo, jwtService);

    }

    @Test
    @DisplayName("Ensure that movies should return Page of movies if the token in the authorization header is valid")
    public void getMoviesValid() {
        String bearerValidToken= "Bearer validToken";
        String validToken = "validToken";
        when(request.getHeader("Authorization")).thenReturn(bearerValidToken);
        when(jwtService.validateToken(validToken)).thenReturn(true);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("rating").ascending());
        List<Movie> expectedMovies = List.of(new Movie(), new Movie());
        Page<Movie> page = new PageImpl<>(expectedMovies, pageable, expectedMovies.size());

        when(movieRepo.findAll(pageable)).thenReturn(page);

        List<Movie> movies = movieService.getMovies(request, 0);

        assertEquals(expectedMovies, movies);
    }


    @Test
    @DisplayName("Ensure that movies should throw an Exception if the token in the authorization header is invalid")
    public void getMoviesInvalid() {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtService.validateToken("invalidToken")).thenReturn(false);
        assertThrows(TokenValidationException.class, () -> movieService.getMovies(request, 0));
    }


    @Test
    @DisplayName("Check that getToken method returns the token in the authorization header")
    public void getToken() {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        String token = movieService.getToken(request);
        assertEquals("validToken", token);
    }




    }



