package com.homeass.deduplication;


import com.homeass.deduplication.common.Pipeline;
import com.homeass.deduplication.movies.MatchingMoviesPair;
import com.homeass.deduplication.movies.Movie;
import com.homeass.deduplication.movies.MovieNormalizer;
import com.homeass.deduplication.parser.CsvReader;
import io.vavr.CheckedFunction0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MoviesDeduplication implements CommandLineRunner {

    CsvReader<Movie> movieCsvReader;
    CsvWriter<MatchingMoviesPair> movieCsvWriter;
    MovieNormalizer movieNormalizer;
    MoviesMatcher moviesMatcher;

    @Autowired
    public MoviesDeduplication(CsvReader<Movie> movieCsvReader, CsvWriter<MatchingMoviesPair> movieCsvWriter, MovieNormalizer movieNormalizer, MoviesMatcher moviesMatcher) {
        this.movieCsvReader = movieCsvReader;
        this.movieCsvWriter = movieCsvWriter;
        this.movieNormalizer = movieNormalizer;
        this.moviesMatcher = moviesMatcher;
    }

    @Override
    public void run(String... args) throws Exception {
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);

        readAndNormalizeMoviesFromCsv(inputFile)
                .map(this::findMatchingMovies)
                .map(writeMatchingMoviesPairsToCsvFunc(outputFile));
    }


    private Pipeline<List<Movie>> readAndNormalizeMoviesFromCsv(File filePath) throws java.io.IOException {
        return Pipeline.of(
                movieCsvReader.read(new FileInputStream(filePath))
                        .stream()
                        .map(movie -> movieNormalizer.replaceNullWithEmptyList(movie))
                        .map(movie -> movieNormalizer.sortFields(movie))
                        .collect(Collectors.toList()));
    }

    private List<MatchingMoviesPair> findMatchingMovies(List<Movie> movieList) {
        return Pipeline.of(movieList)
                .map(moviesMatcher::findMatches)
                .map(moviesMatcher::filterDuplicates)
                .getValue();
    }

    private Function<List<MatchingMoviesPair>, File> writeMatchingMoviesPairsToCsvFunc(File outputFile) {
        return (List<MatchingMoviesPair> list) -> uncheckException(outputFile, list);
    }

    private File uncheckException(File outputFile, List<MatchingMoviesPair> list) {
        return CheckedFunction0.of(() -> writeMatchingMoviesPairsToCsvFunc(outputFile, list)).unchecked().apply();
    }

    private File writeMatchingMoviesPairsToCsvFunc(File output, List<MatchingMoviesPair> matchingMoviesPairs) throws IOException {
        return movieCsvWriter.write(output, matchingMoviesPairs);
    }


}
