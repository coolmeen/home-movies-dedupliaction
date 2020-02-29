package com.homeass.deduplication.movies;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer year;
    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer length;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> genre;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> directors;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> actors;

}
