package com.homeass.deduplication.movies.configuration;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.homeass.deduplication.movies.entity.MatchingMovieToCsv;
import com.homeass.deduplication.movies.entity.MatchingMovies;
import com.homeass.deduplication.movies.entity.Movie;
import com.homeass.deduplication.parser.CsvReader;
import com.homeass.deduplication.parser.CsvWriter;
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
    public CsvWriter<MatchingMovieToCsv> csvWriter(ObjectWriter objectWriter) {
        return new CsvWriter<>(objectWriter, MatchingMovieToCsv.class);
    }
}
