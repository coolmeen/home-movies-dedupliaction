package com.homeass.deduplication;

import com.fasterxml.jackson.databind.ObjectReader;
import com.homeass.deduplication.movies.Movie;
import com.homeass.deduplication.parser.CsvParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovieParserConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public CsvParser<Movie> csvParser(ObjectReader objectReader) {
        return new CsvParser<>(objectReader,Movie.class);
    }
}
