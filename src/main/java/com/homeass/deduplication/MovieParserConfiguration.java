package com.homeass.deduplication;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.homeass.deduplication.movies.MatchingMoviesPair;
import com.homeass.deduplication.movies.Movie;
import com.homeass.deduplication.parser.CsvReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovieParserConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public CsvReader<Movie> csvReader(ObjectReader objectReader) {
        return new CsvReader<>(objectReader,Movie.class);
    }

    @ConditionalOnMissingBean
    @Bean
    public CsvWriter<MatchingMoviesPair> csvWriter(ObjectWriter objectWriter) {
        return new CsvWriter<>(objectWriter, MatchingMoviesPair.class);
    }
}
