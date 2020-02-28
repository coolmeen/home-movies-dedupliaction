package com.homeass.deduplication.parser.cofiguration;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.*;
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
        return new CsvMapper().enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.WRAP_AS_ARRAY).reader(schema);
    }



}
