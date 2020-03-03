package com.homeass.deduplication.movies.matcher;


import java.util.List;
import java.util.Set;

public interface MatchingStrategy<T> {
    List<Set<T>> findMatches(List<T> listToMatch);
}
