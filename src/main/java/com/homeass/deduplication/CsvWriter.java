package com.homeass.deduplication;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.homeass.deduplication.movies.MatchingMoviesPair;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CsvWriter<T> {

    private final ObjectWriter objectWriter;
    private final Class<T> tclass;

    public CsvWriter(ObjectWriter objectWriter, Class<T> matchingMoviesPairClass) {
        this.objectWriter = objectWriter;
        this.tclass = matchingMoviesPairClass;
    }

    public File write(File file,List<T> outputData) throws IOException {
        objectWriter.forType(tclass).writeValues(file).writeAll(outputData);
        return file;
    }
}
