package com.homeass.deduplication.movies.entity;

import com.sun.istack.internal.NotNull;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@With
@Data
public class Movie {
    @NotNull private String id;
    @NotNull private Integer year;
    @NotNull private Double length;
    private List<String> genre;
    private List<String> directors;
    private List<String> actors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (!id.equals(movie.id)) return false;
        if (!year.equals(movie.year)) return false;
        if (!length.equals(movie.length)) return false;
        if (genre != null ? !genre.equals(movie.genre) : movie.genre != null) return false;
        if (directors != null ? !directors.equals(movie.directors) : movie.directors != null) return false;
        return actors != null ? actors.equals(movie.actors) : movie.actors == null;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}