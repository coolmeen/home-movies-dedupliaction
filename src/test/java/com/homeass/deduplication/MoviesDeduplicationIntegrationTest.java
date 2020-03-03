package com.homeass.deduplication;

import com.homeass.deduplication.movies.entity.MatchingMovieToCsv;
import com.homeass.deduplication.movies.entity.Movie;
import com.homeass.deduplication.movies.matcher.MoviesMatchingService;
import com.homeass.deduplication.movies.service.MovieNormalizer;
import com.homeass.deduplication.parser.CsvReader;
import com.homeass.deduplication.parser.CsvWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(MoviesDeduplication.class)
class MoviesDeduplicationIntegrationTest {


    @Autowired
    CsvReader<Movie> movieCsvReader;
    @Autowired
    CsvWriter<MatchingMovieToCsv> movieCsvWriter;
    @Autowired
    MovieNormalizer movieNormalizer;
    @Autowired
    MoviesMatchingService moviesMatchingService;



    @Test
    void whenAllMoviesMatchShouldCreate1MatchingList() throws Exception {
        MoviesDeduplication moviesDeduplication = new MoviesDeduplication(movieCsvReader, movieCsvWriter, movieNormalizer, moviesMatchingService);
        moviesDeduplication.run("src/test/resources/test2.tsv","file");

    }
}