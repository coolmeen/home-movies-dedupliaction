package com.homeass.deduplication.movies.matcher;

import com.homeass.deduplication.movies.entity.Movie;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoviePredicates {
    public Integer matchingYearFloor(Movie movie) {
        return movie.getYear() - 1;
    }

    public Integer matchingYearCeiling(Movie movie) {
        return movie.getYear() + 1;
    }

    public Double matchingLengthFloor(Movie movie) {
        return movie.getLength() * 0.95;
    }

    public Double matchingLengthCeiling(Movie movie) {
        return movie.getLength() * 1.05;
    }

    public boolean match(Movie m1, Movie m2) {
        return yearPredicate(m1, m2) &&
                lengthPredicate(m1, m2)
                && testSubLists(m1.getGenre(), m2.getGenre())
                && testSubLists(m1.getDirectors(), m2.getDirectors())
                && testSubLists(m1.getActors(), m2.getActors());
    }

    private boolean lengthPredicate(Movie m1, Movie m2) {
        Double diff = Math.abs(m1.getLength() - m2.getLength());
        Double min = Math.min(m1.getLength(), m2.getLength());
        Double max = Math.max(m1.getLength(), m2.getLength());
        return (diff / min) < 0.051 || (diff / max) < 0.051;
    }

    private boolean yearPredicate(Movie m1, Movie m2) {
        return Math.abs(m1.getYear() - m2.getYear()) <= 1;
    }

    boolean testSubLists(@NotNull List<String> l1, @NotNull List<String> l2) {
        if (l1.isEmpty() && l2.isEmpty())
            return true;

        if (l1.isEmpty() && l2.size() == 1)
            return true;

        if (l2.isEmpty() && l1.size() == 1)
            return true;

        if (l1.isEmpty() || l2.isEmpty())
            return false;

        if (l1.size() > l2.size())
            return l1.containsAll(l2);
        else
            return l2.containsAll(l1);

    }
}
