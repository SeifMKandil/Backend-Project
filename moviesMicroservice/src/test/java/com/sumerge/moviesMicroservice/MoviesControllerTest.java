package com.sumerge.moviesMicroservice;

import com.sumerge.moviesMicroservice.controllers.MovieController;
import com.sumerge.moviesMicroservice.models.Movie;
import com.sumerge.moviesMicroservice.services.JwtService;
import com.sumerge.moviesMicroservice.services.MovieService;
import com.sumerge.moviesMicroservice.services.repository.MovieRepo;
import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static java.nio.file.Paths.get;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MoviesControllerTest {
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
        movieController = Mockito.mock(MovieController.class);
        jwtService = Mockito.mock(JwtService.class);
        request = Mockito.mock(HttpServletRequest.class);
        movieService = new MovieService(movieRepo, jwtService);
    }

        @Test
        @DisplayName("When passing a valid id it should return a movie with the correct data")
        public void movieId() throws Exception{
        long movieId = 52L;
        Movie expectedMovie = new Movie();
        expectedMovie.setId(movieId);
        expectedMovie.setName("The Mask");
        expectedMovie.setDescription("This the movie description");
        expectedMovie.setRating(4.2);
        expectedMovie.setType("Comedy");

        when(movieRepo.findById(movieId)).thenReturn(Optional.of(expectedMovie));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/movie/movies/{id}" , movieId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.id").value( expectedMovie.getId())

        ).andExpect(jsonPath("$.name").value( expectedMovie.getName())

        ).andExpect(jsonPath("$.description").value( expectedMovie.getDescription())

        ).andExpect(jsonPath("$.rating").value( expectedMovie.getRating())

        ).andExpect(jsonPath("$.type").value( expectedMovie.getType()) );

    }

    @Test
    @DisplayName("When trying to add a movie without passing the Jwt token it should return 401 Unauthorized")
    public void addMovieInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/movie/addMovie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Camp\", \"description\": \"This a horror movie\", \"rating\": \"4.1\", \"type\":\"Horror\" }"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("When trying to add a movie by passing the Jwt token it should return status ok")
    public void addMovieValid() throws Exception {
        String validToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIb3NzYW1AZ21haWwuY29tIiwiaWF0IjoxNjk3NzAyNjY1LCJleHAiOjE2OTc3MDQxMDV9.gKltXqZpb5BW9Ei8GhlP-WWboL9LzKsSdyOm7oa013A";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/movie/addMovie").header("Authorization" , validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Example\", \"description\": \"This a horror movie\", \"rating\": \"4.1\", \"type\":\"Horror\" }")
                        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When trying to add a movie by passing an invalid  Jwt token it should return 401 Unauthorized")
    public void addMovieInValid() throws Exception {
        String validToken = "Bearer InvalidToken";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/movie/addMovie").header("Authorization" , validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Example\", \"description\": \"This a horror movie\", \"rating\": \"4.1\", \"type\":\"Horror\" }")
                )
                .andExpect(status().isUnauthorized());
    }




}
