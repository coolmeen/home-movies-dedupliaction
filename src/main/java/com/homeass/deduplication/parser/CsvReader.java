package com.homeass.deduplication.parser;

import com.fasterxml.jackson.databind.ObjectReader;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@AllArgsConstructor
public class CsvReader<T> {
    private ObjectReader csvParser;
    private Class<T> tClass;

    public List<T> read(@NotNull InputStream file) throws IOException {
        return (List<T>) csvParser.forType(tClass).readValues(file).readAll();
    }

}
