package com.homeass.deduplication.common.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor
public class Edge {
    private Integer from;
    private Integer to;

}