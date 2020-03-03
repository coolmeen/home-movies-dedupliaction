package com.homeass.deduplication.parser.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("parsing")
@Data
public class ParseProperties {
    String  columnSeparator;
    String arrayElementSeparator;
    String nullValue;
    boolean failOnDataCorruption;
    boolean ignoreDataCorruption;

}
