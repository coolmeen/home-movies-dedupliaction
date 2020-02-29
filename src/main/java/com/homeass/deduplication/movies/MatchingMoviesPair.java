package com.homeass.deduplication.movies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchingMoviesPair {
    Movie leftMovie;
    Movie rightMovie;
}
