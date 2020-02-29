package com.homeass.deduplication.movies;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@With
@EqualsAndHashCode
public class Movie {
    @NonNull
    private String id;
    @NonNull
    private Integer year;
    @NonNull
    private Integer length;
    private List<String> genre;
    private List<String> directors;
    private List<String> actors;

}
