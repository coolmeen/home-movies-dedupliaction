package com.homeass.deduplication.parser;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.*;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ParseProperties.class)
public class ParserConfiguration {

    @ConditionalOnMissingBean
    @Bean
    ObjectReader csvObjectReader(ParseProperties properties) {
        CsvSchema schema = CsvSchema.emptySchema()
                .withHeader()
                .withNullValue(properties.getNullValue())
                .withArrayElementSeparator(properties.getArrayElementSeparator())
                .withColumnSeparator(properties.getColumnSeparator().charAt(0));
        return new CsvMapper()
                .enable(CsvParser.Feature.WRAP_AS_ARRAY)
                .enable(CsvParser.Feature.SKIP_EMPTY_LINES)
                .reader(schema);
    }


    @ConditionalOnMissingBean
    @Bean
    ObjectWriter csvObjectWriter(ParseProperties properties) {
        CsvSchema schema = CsvSchema.emptySchema()
                .withoutHeader()
                .withColumnSeparator(properties.getColumnSeparator().charAt(0))
                .withColumnReordering(true);

        return new CsvMapper()
                .enable(CsvParser.Feature.SKIP_EMPTY_LINES)
                .writer(schema);
    }


}
