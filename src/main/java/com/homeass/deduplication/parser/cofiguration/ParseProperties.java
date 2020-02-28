package com.homeass.deduplication.parser.cofiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties("parsing")
@Data
public class ParseProperties {
    File file;
    String  columnSeparator;
    String arrayElementSeparator;
    String nullValue;
    boolean failOnDataCorruption;
    boolean ignoreDataCorruption;

}
