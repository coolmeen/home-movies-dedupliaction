package com.homeass.deduplication.movies.matcher;

import com.homeass.deduplication.movies.entity.Movie;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Slf4j
@Component
public class MoviesMatchingStrategy implements MatchingStrategy<Movie> {

    MoviePredicates moviePredicates;

    @Autowired
    public MoviesMatchingStrategy(MoviePredicates moviePredicates) {
        this.moviePredicates = moviePredicates;
    }

    @Override
    public List<Set<Movie>> findMatches(List<Movie> listToMatch) {
        TreeMap<Pair<Integer, Double>, List<Movie>> invertedIndex = buildSearchIndex(listToMatch);
        return listToMatch.stream()
                .map(movie -> findAllMatchingMovies(movie, invertedIndex))
                .collect(Collectors.toList());
    }

    private Set<Movie> findAllMatchingMovies(Movie movie, TreeMap<Pair<Integer, Double>, List<Movie>> movieIndex) {
        return extractMatchingMoviesFromIndex(movie, movieIndex)
                .parallel()
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .filter(matchingMovie -> moviePredicates.match(movie, matchingMovie))
                .collect(toSet());
    }

    private Stream<Collection<List<Movie>>> extractMatchingMoviesFromIndex(Movie movie, TreeMap<Pair<Integer, Double>, List<Movie>> invertedIndex) {
        return Stream.of(
                invertedIndex.subMap(
                        new Pair<>(moviePredicates.matchingYearFloor(movie), moviePredicates.matchingLengthFloor(movie)), true,
                        new Pair<>(moviePredicates.matchingYearFloor(movie), moviePredicates.matchingLengthCeiling(movie)), true)
                        .values(),
                invertedIndex.subMap(
                        new Pair<>(movie.getYear(), moviePredicates.matchingLengthFloor(movie)), true,
                        new Pair<>(movie.getYear(), moviePredicates.matchingLengthCeiling(movie)), true)
                        .values(),
                invertedIndex.subMap(
                        new Pair<>(moviePredicates.matchingYearCeiling(movie), moviePredicates.matchingLengthFloor(movie)), true,
                        new Pair<>(moviePredicates.matchingYearCeiling(movie), moviePredicates.matchingLengthCeiling(movie)), true)
                        .values());
    }

    private TreeMap<Pair<Integer, Double>, List<Movie>> buildSearchIndex(List<Movie> listToMatch) {
        listToMatch.sort((o1, o2) -> {
            if(o1.getYear().equals(o2.getYear()))
                return (int)(o1.getLength() - o2.getLength());

            return o1.getYear()-o2.getYear();
        });
        return listToMatch.stream()
                .collect(groupingBy((Movie movie) -> new Pair<>(movie.getYear(), Double.valueOf(movie.getLength()))
                        , () -> new TreeMap<>(createCompositeKeyComparator())
                        , mapping((Movie movie) -> movie, toList())));
    }

    private Comparator<Pair<Integer, Double>> createCompositeKeyComparator() {
        return (Pair<Integer, Double> o1, Pair<Integer, Double> o2) -> {
            if (o1.getKey().equals(o2.getKey()))
                return (int) (o1.getValue() - o2.getValue());
            else
                return o1.getKey() - o2.getKey();
        };
    }
}
