package com.homeass.deduplication.common.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vertex<T>{
    private T value;
    private Set<Vertex<T>> neighbors;
    private Vertex<T> parent;
    private boolean visited;


}