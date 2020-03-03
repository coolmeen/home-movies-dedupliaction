package com.homeass.deduplication;


import com.homeass.deduplication.common.Pipeline;
import com.homeass.deduplication.movies.entity.MatchingMovieToCsv;
import com.homeass.deduplication.movies.matcher.MoviesMatchingService;
import com.homeass.deduplication.movies.entity.MatchingMovies;
import com.homeass.deduplication.movies.entity.Movie;
import com.homeass.deduplication.movies.service.MovieNormalizer;
import com.homeass.deduplication.parser.CsvReader;
import com.homeass.deduplication.parser.CsvWriter;
import io.vavr.CheckedFunction0;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Profile("main")
@Slf4j
public class MoviesDeduplication implements CommandLineRunner {

    CsvReader<Movie> movieCsvReader;
    CsvWriter<MatchingMovieToCsv> movieCsvWriter;
    MovieNormalizer movieNormalizer;
    MoviesMatchingService moviesMatchingService;

    @Autowired
    public MoviesDeduplication(CsvReader<Movie> movieCsvReader, CsvWriter<MatchingMovieToCsv> movieCsvWriter, MovieNormalizer movieNormalizer, MoviesMatchingService moviesMatchingService) {
        this.movieCsvReader = movieCsvReader;
        this.movieCsvWriter = movieCsvWriter;
        this.movieNormalizer = movieNormalizer;
        this.moviesMatchingService = moviesMatchingService;
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

    private List<MatchingMovies> findMatchingMovies(List<Movie> movieList) {
        log.info("finished reading from csv : " + movieList.size() + " elements");
        return Pipeline.of(movieList)
                .map(moviesMatchingService::findMatches)
                .map(moviesMatchingService::filterDuplicates)
                .map(matchingList -> moviesMatchingService.mergeMatchingListWithCommonElements(movieList,matchingList))
                .getValue();
    }

    private Function<List<MatchingMovies>, OutputStream> writeMatchingMoviesPairsToCsvFunc(File outputFile) {
        return (List<MatchingMovies> list) -> uncheckException(outputFile, list);
    }

    private OutputStream uncheckException(File outputFile, List<MatchingMovies> list) {
        return CheckedFunction0.of(() -> writeMatchingMoviesPairsToCsvFunc(outputFile, list)).unchecked().apply();
    }

    private OutputStream writeMatchingMoviesPairsToCsvFunc(File output, List<MatchingMovies> matchingMovies) throws IOException {
        log.info("found  matching: " + matchingMovies.size() + " movies");
        List<MatchingMovieToCsv> matchingMovieToCsvList = matchingMovies.stream()
                .map(matchingMovie -> new MatchingMovieToCsv(matchingMovie.getListOfMatchingMovies()
                        .stream()
                        .map(Movie::getId)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
        return movieCsvWriter.write(new FileOutputStream(output), matchingMovieToCsvList);
    }


}
