package com.homeass.deduplication.parser.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.*;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.homeass.deduplication.movies.entity.MatchingMovieToCsv;
import com.homeass.deduplication.movies.entity.Movie;
import com.homeass.deduplication.parser.CsvWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.IGNORE_UNKNOWN;

@Configuration
@EnableConfigurationProperties(ParseProperties.class)
public class ParserConfiguration {

    @ConditionalOnMissingBean
    @Bean
    ObjectReader csvObjectReader(ParseProperties properties) {
        CsvSchema schema = CsvSchema.emptySchema()
                .withHeader()
                .withNullValue("\\N")
                .withArrayElementSeparator(",")
                .withColumnSeparator('\t');
        return new CsvMapper()

                .enable(CsvParser.Feature.WRAP_AS_ARRAY)
                .readerFor(Movie.class)
                .with(schema);
    }


    @ConditionalOnMissingBean
    @Bean
    ObjectWriter csvObjectWriter(ParseProperties properties) {
        return new CsvMapper()
                .enable(CsvParser.Feature.WRAP_AS_ARRAY)
                .enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
                .writer(new CsvMapper()
                        .schemaFor(MatchingMovieToCsv.class)//this is highly coupled but i couldn't work around it :(
                        .withArrayElementSeparator("\t")
                        .withoutQuoteChar()
                        .withColumnSeparator(properties.getColumnSeparator().charAt(0)));
    }


}
