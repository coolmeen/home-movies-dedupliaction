package com.homeass.deduplication.movies;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                .length(89)
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
                .length(89)
                .year(2013)
                .genre(Collections.singletonList("drama"))
                .directors(Collections.singletonList("Lina Chamie"))
                .actors(unsortedActors)
                .build();
        Movie output = movieNormalizer.sortFields(input);
        Assertions.assertEquals(input.getActors(),sortedActors);
    }
}