package com.homeass.deduplication.movies;

import com.homeass.deduplication.movies.entity.Movie;
import com.homeass.deduplication.movies.service.MovieNormalizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MovieNormalizerTest {


    private static MovieNormalizer movieNormalizer;

    @BeforeAll

    public static void init(){
        movieNormalizer = new MovieNormalizer();
    }
    @Test
    void whenNullGenereReplaceWithEmptyList() {
        Movie input = Movie.builder()
                .id("tt2355936")
                .length(89d)
                .year(2013)
                .genre(null)
                .directors(Collections.singletonList("Lina Chamie"))
                .actors(Arrays.asList("Davi Galdeano", "Gregório Mussatti Cesare", "Dira Paes", "Julia Weiss", "Antônia Ricca", "Marco Ricca", "Lucas Zamberlan"))
                .build();
        Movie output = movieNormalizer.replaceNullWithEmptyList(input);
        Assertions.assertNotEquals(input,output);
        input.setGenre(Collections.emptyList());
        Assertions.assertEquals(input,output);
    }

    @Test
    void sortFields() {
        List<String> unsortedActors = Arrays.asList("c","a","b");
        List<String> sortedActors = Arrays.asList("a","b","c");
        Movie input = Movie.builder()
                .id("tt2355936")
                .length(89d)
                .year(2013)
                .genre(Collections.singletonList("drama"))
                .directors(Collections.singletonList("Lina Chamie"))
                .actors(unsortedActors)
                .build();
        movieNormalizer.sortFields(input);
        Assertions.assertEquals(input.getActors(),sortedActors);
    }
}