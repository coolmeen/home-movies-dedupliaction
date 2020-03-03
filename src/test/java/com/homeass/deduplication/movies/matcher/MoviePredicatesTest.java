package com.homeass.deduplication.movies.matcher;

import com.homeass.deduplication.movies.entity.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;

class MoviePredicatesTest {

    private static MoviePredicates predicates;

    @BeforeAll
    public static void init() {
        predicates = new MoviePredicates();
    }

    @Nested
    @DisplayName("test form matching movies")
    class match {

        @Test
        @DisplayName("when only id is different should match")
        public void whenOnlyIdDiffer() {
            Movie movie1 = initMovie1();
            Movie movie2 = initMovie2();

            Assert.isTrue(predicates.match(movie1, movie2), "movies should match");
        }

        @Test
        @DisplayName("when all fields match should match")
        public void whenAllFieldsMatch() {
            Movie movie1 = initMovie1();
            Movie movie2 = initMovie2();

            Assert.isTrue(predicates.match(movie1, movie2), "movies should match");
        }

        @Test
        @DisplayName("when movies with 1 delta is different should match")
        public void whenOMovieDifferBy1() {
            Movie movie1 = initMovie1();
            Movie movie2 = initMovie2();
            Assert.isTrue(predicates.match(movie1, movie2), "movies should match");

            movie1.setYear(predicates.matchingYearFloor(movie2));
            Assert.isTrue(predicates.match(movie1, movie2), "movies should match");

            movie1.setYear(predicates.matchingYearCeiling(movie2));
            Assert.isTrue(predicates.match(movie1, movie2), "movies should match");
        }

        @Test
        @DisplayName("when length differ by 5 should match")
        public void whenOLengthDiffer() {
            Movie movie1 = initMovie1();
            Movie movie2 = initMovie2();

            Assert.isTrue(predicates.match(movie1, movie2), "movies should match");

            movie1.setLength(predicates.matchingLengthFloor(movie2));
            Assert.isTrue(predicates.match(movie1, movie2), "movies should match");

            movie1.setLength(predicates.matchingLengthCeiling(movie2));
            Assert.isTrue(predicates.match(movie1, movie2), "movies should match");
        }

        @Nested
        @DisplayName("test actors/genere/directors lists matching logic")
        class lists {
            @Test
            @DisplayName("when both actors list empty should match")
            public void whenActorsListEmpty() {
                Movie movie1 = initMovie1();
                Movie movie2 = initMovie2();

                Assert.isTrue(predicates.match(movie1, movie2), "movies should match");

                movie1.setActors(Collections.emptyList());
                movie2.setActors(Collections.emptyList());
                Assert.isTrue(predicates.match(movie1, movie2), "movies should match");
            }

            @Test
            @DisplayName("when one actors list is empty and another length is 1 should match")
            public void whenActorsListEmptyAndLength1() {
                Movie movie1 = initMovie1();
                Movie movie2 = initMovie2();

                Assert.isTrue(predicates.match(movie1, movie2), "movies should match");

                movie1.setActors(Collections.emptyList());
                movie2.setActors(Collections.singletonList("dani devito"));
                Assert.isTrue(predicates.match(movie1, movie2), "movies should match");
            }

            @Test
            @DisplayName("when one actors list is sublistOfAnother should match")
            public void whenActorsSubListOfAnother() {
                Movie movie1 = initMovie1();
                Movie movie2 = initMovie2();

                Assert.isTrue(predicates.match(movie1, movie2), "movies should match");

                movie1.setActors(Arrays.asList("dani devito", "lala"));
                movie2.setActors(Arrays.asList("dani devito", "dada", "lala"));
                Assert.isTrue(predicates.match(movie1, movie2), "movies should match");
            }

        }
    }

    @Nested
    @DisplayName("test for matching movies")
    class notmatch {



        @Test
        @DisplayName("when movies with 2 delta is different should not match")
        public void whenOMovieDifferBy1() {
            Movie movie1 = initMovie1();
            Movie movie2 = initMovie2();
            Assert.isTrue(predicates.match(movie1, movie2), "movies should  match");

            movie1.setYear(predicates.matchingYearFloor(movie2) -1 );
            Assert.isTrue(!predicates.match(movie1, movie2), "movies should not match");

            movie1.setYear(predicates.matchingYearCeiling(movie2) + 1);
            Assert.isTrue(!predicates.match(movie1, movie2), "movies should not match");
        }

        @Test
        @DisplayName("when length differ by 5 + 1  should not match")
        public void whenOLengthDiffer() {
            Movie movie1 = initMovie1();
            Movie movie2 = initMovie2();

            Assert.isTrue(predicates.match(movie1, movie2), "movies should not match");

            movie1.setLength(predicates.matchingLengthFloor(movie2) - 1);
            Assert.isTrue(!predicates.match(movie1, movie2), "movies should not match");

            movie1.setLength(predicates.matchingLengthCeiling(movie2) + 1);
            Assert.isTrue(!predicates.match(movie1, movie2), "movies should not match");
        }


        @Nested
        @DisplayName("test actors/genere/directors lists matching logic")
        class lists {
            @Test
            @DisplayName("when only actors list is empty and another is size of 2 should not match")
            public void whenActorsListEmpty() {
                Movie movie1 = initMovie1();
                Movie movie2 = initMovie2();

                Assert.isTrue(predicates.match(movie1, movie2), "movies should  match");

                movie1.setActors(Collections.emptyList());
                movie2.setActors(Arrays.asList("dude","dada"));
                Assert.isTrue(!predicates.match(movie1, movie2), "movies should not match");
            }

            @Test
            @DisplayName("when one actors list is not sublist of another")
            public void whenActorsListEmptyAndLength1() {
                Movie movie1 = initMovie1();
                Movie movie2 = initMovie2();

                Assert.isTrue(predicates.match(movie1, movie2), "movies should match");

                movie1.setActors(Arrays.asList("dani devito","dada"));
                movie2.setActors(Arrays.asList("dani devito","lala"));
                Assert.isTrue(!predicates.match(movie1, movie2), "movies should match");
            }



            @Test
            @DisplayName("when only one field doesn't match should not match")
            public void whenActorsSubListOfAnother2() {
                Movie movie1 = initMovie1();
                Movie movie2 = initMovie1();

                Assert.isTrue(predicates.match(movie1, movie2), "movies should match");

                movie1.setActors(Arrays.asList("dani devito", "lala"));
                movie2.setActors(Arrays.asList("dani devito", "dada", "lalaaa"));
                Assert.isTrue(!predicates.match(movie1, movie2), "movies should match");
            }
        }
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
                .actors(Arrays.asList("Davi Galdeano", "Gregório Mussatti Cesare", "Dira Paes", "Julia Weiss", "Antônia Ricca", "Marco Ricca", "Lucas Zamberlan"))
                .build();
    }
}