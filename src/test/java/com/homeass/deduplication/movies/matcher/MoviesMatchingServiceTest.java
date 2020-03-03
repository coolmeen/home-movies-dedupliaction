package com.homeass.deduplication.movies.matcher;

import com.homeass.deduplication.movies.entity.MatchingMovies;
import com.homeass.deduplication.movies.entity.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.*;

@SpringBootTest
@Import({MoviesMatchingService.class})
class MoviesMatchingServiceTest {

    @Autowired
    MoviesMatchingService moviesMatchingService;

    @Autowired
    MatchingStrategy<Movie> movieMatchingStrategy;

    @Autowired
    MoviePredicates predicates;

    @Test
    public void whenTwoMatchingListTheSameShouldFilterOne(){

        Movie movie1 = initMovie1();
        Movie movie2 = initMovie2();
        MatchingMovies movies = new MatchingMovies(new HashSet<>(Arrays.asList(movie1, movie2)), UUID.randomUUID().toString());
        MatchingMovies moviesReversed = new MatchingMovies(new HashSet<>(Arrays.asList(movie2, movie1)), UUID.randomUUID().toString());
        List<MatchingMovies> matchingMovies = moviesMatchingService.filterDuplicates(Arrays.asList(movies, moviesReversed));
        Assertions.assertNotNull(matchingMovies);
        Assertions.assertEquals(1,matchingMovies.size());
        Assertions.assertTrue(matchingMovies.contains(movies));
    }

    @Test
    public void whenTwoMatchingListNotTheSameShouldReturnBoth(){
        Movie movie1 = initMovie1();
        Movie movie2 = initMovie2();
        Movie movie3 = initMovie3();
        Movie movie4 = initMovie4();
        MatchingMovies movies1 = new MatchingMovies(new HashSet<>(Arrays.asList(movie1, movie2)), UUID.randomUUID().toString());
        MatchingMovies movies2 = new MatchingMovies(new HashSet<>(Arrays.asList(movie3, movie4)), UUID.randomUUID().toString());
        List<MatchingMovies> matchingMovies = moviesMatchingService.filterDuplicates(Arrays.asList(movies1, movies2));
        Assertions.assertNotNull(matchingMovies);
        Assertions.assertEquals(2,matchingMovies.size());
        Assertions.assertTrue(matchingMovies.contains(movies1));
        Assertions.assertTrue(matchingMovies.contains(movies2));
    }

    @Test
    public void whenTwoMatchingShouldMatch(){
        Movie movie1 = initMovie1();
        Movie movie2 = initMovie2();
        Assertions.assertTrue(predicates.match(movie1,movie2));
        List<MatchingMovies> matchingMovies = moviesMatchingService.findMatches(Arrays.asList(movie1, movie2));
        Assertions.assertEquals(2,matchingMovies.size());
        MatchingMovies movies = matchingMovies.get(0);
        Assertions.assertTrue(movies.getListOfMatchingMovies().contains(movie1));
        Assertions.assertTrue(movies.getListOfMatchingMovies().contains(movie2));

    }



    private Movie initMovie1() {
        return Movie.builder()
                .id("tt2355936")
                .length(89d)
                .year(2013)
                .genre(Collections.singletonList("happy"))
                .directors(Collections.singletonList("Lina Chamie"))
                .actors(Arrays.asList("Davi Galdeano", "Gregório Mussatti Cesare", "Dira Paes", "Julia Weiss", "Antônia Ricca", "Marco Ricca", "Lucas Zamberlan"))
                .build();
    }

    private Movie initMovie2() {
        return Movie.builder()
                .id("tt2355938")
                .length(89d)
                .year(2013)
                .genre(Collections.singletonList("happy"))
                .directors(Collections.singletonList("Lina Chamie"))
                .actors(Arrays.asList("Davi Galdeano", "Gregório Mussatti Cesare", "Julia Weiss", "Antônia Ricca", "Marco Ricca", "Lucas Zamberlan"))
                .build();
    }
    private Movie initMovie3() {
        return Movie.builder()
                .id("tt2355934")
                .length(89d)
                .year(2013)
                .genre(null)
                .directors(Collections.singletonList("Lina Chamie"))
                .actors(Arrays.asList("Davi Galdeano", "Gregório Mussatti Cesare", "Dira Paes", "Julia Weiss", "Antônia Ricca", "Marco Ricca", "Lucas Zamberlan"))
                .build();
    } private Movie initMovie4() {
        return Movie.builder()
                .id("tt2355931")
                .length(89d)
                .year(2013)
                .genre(null)
                .directors(Collections.singletonList("Lina Chamie"))
                .actors(Arrays.asList("Davi Galdeano", "Gregório Mussatti Cesare", "Dira Paes", "Julia Weiss", "Antônia Ricca", "Marco Ricca", "Lucas Zamberlan"))
                .build();
    }

   // @Configuration
    static class config {

        @Bean
        @Primary
        MatchingStrategy<?> movieMatchingStrategy(){
            return Mockito.mock(MatchingStrategy.class);
        }
    }


}