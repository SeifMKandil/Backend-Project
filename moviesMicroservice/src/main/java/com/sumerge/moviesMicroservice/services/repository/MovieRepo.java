package com.sumerge.moviesMicroservice.services.repository;

import com.sumerge.moviesMicroservice.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MovieRepo extends JpaRepository<Movie , Long> {
}
