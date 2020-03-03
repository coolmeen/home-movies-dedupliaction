package com.homeass.deduplication.movies.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class MatchingMovies {
    @JsonUnwrapped
    Set<Movie> listOfMatchingMovies;
    String id;


    public MatchingMovies(Set<Movie> listOfMatchingMovies, String id) {
        this.listOfMatchingMovies = listOfMatchingMovies;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchingMovies that = (MatchingMovies) o;

        if (!id.equals(that.id)) return false;
        return listOfMatchingMovies.equals(that.listOfMatchingMovies);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
