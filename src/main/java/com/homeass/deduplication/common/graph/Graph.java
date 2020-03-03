package com.homeass.deduplication.common.graph;

import com.homeass.deduplication.movies.entity.MatchingMovies;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Graph {
    private Map<MatchingMovies, Set<MatchingMovies>> vertexMap;

    public Graph(int size) {
        this.vertexMap = new HashMap<>(size);
    }

    public boolean addVertex(MatchingMovies v) {
        vertexMap.put(v, new HashSet<>());
        return true;
    }


    public Set<MatchingMovies> getVertices() {
        return vertexMap.keySet();
    }

    public Set<MatchingMovies> getNeighbors(MatchingMovies ver) {
        return vertexMap.get(ver);
    }


    public void addEdge(MatchingMovies v1, MatchingMovies v2) {
        if(v1 != v2)
            vertexMap.get(v1).add(v2);
    }

}
