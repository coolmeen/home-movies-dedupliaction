package com.homeass.deduplication;

import reactor.util.function.Tuple2;

import java.util.List;

public interface MatchingStrategy<T> {
    List<Tuple2<T,T>> findMatches(List<T> listToMatch);
}
