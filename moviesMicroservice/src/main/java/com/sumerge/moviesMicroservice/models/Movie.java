package com.sumerge.moviesMicroservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "movie")
public class Movie
{
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;
    private Double rating;
    private String release;
    private String type;
    private String image;

}
