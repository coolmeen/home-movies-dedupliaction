package com.homeass.deduplication.parser;

import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CsvWriter<T> {

    private final ObjectWriter objectWriter;
    private final Class<T> tclass;

    public CsvWriter(ObjectWriter objectWriter, Class<T> matchingMoviesPairClass) {
        this.objectWriter = objectWriter;
        this.tclass = matchingMoviesPairClass;
    }

    public OutputStream write(OutputStream file, List<T> outputData) throws IOException {
        objectWriter.forType(tclass).writeValues(file).writeAll(outputData);
        return file;
    }
}
