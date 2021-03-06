package com.homeass.deduplication.movies.service;


import com.homeass.deduplication.movies.entity.Movie;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MovieNormalizer {

    public Movie replaceNullWithEmptyList(Movie movie) {
        return movie
                .withGenre(movie.getGenre() == null ? Collections.emptyList() : movie.getGenre())
                .withDirectors(movie.getDirectors() == null ? Collections.emptyList() : movie.getDirectors())
                .withActors(movie.getActors() == null ? Collections.emptyList() : movie.getActors());
    }


    public Movie sortFields(Movie movie) {
        movie.getDirectors().sort(String::compareTo);
        movie.getActors().sort(String::compareTo);
        movie.getGenre().sort(String::compareTo);
        return movie;
    }


}
