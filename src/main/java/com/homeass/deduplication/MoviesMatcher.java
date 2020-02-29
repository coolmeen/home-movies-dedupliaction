package com.homeass.deduplication;

import com.homeass.deduplication.movies.MatchingMoviesPair;
import com.homeass.deduplication.movies.Movie;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoviesMatcher {
    MatchingStrategy<Movie> matchingStrategy;

    public List<MatchingMoviesPair> findMatches(List<Movie> movieList) {
        return matchingStrategy.findMatches(movieList)
                .stream()
                .map(pair -> new MatchingMoviesPair(pair.getT1(), pair.getT2()))
                .collect(Collectors.toList());
    }

    public List<MatchingMoviesPair> filterDuplicates(List<MatchingMoviesPair> matchingList) {
        return matchingList;
    }
}
