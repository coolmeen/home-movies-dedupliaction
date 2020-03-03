package com.homeass.deduplication.common.graph;

import com.homeass.deduplication.movies.entity.MatchingMovies;

import java.util.*;

public class DFS {
    protected Graph graph;
    protected Set<MatchingMovies> visited;

    public DFS(Graph graph) {
        this.graph = graph;
        visited = new HashSet<>(graph.getVertices().size());
    }

    public void processVertex(MatchingMovies source, List<MatchingMovies> path) {
        path.add(source);
    }

    public List<MatchingMovies> searchIterative(MatchingMovies source) {


        if (visited.contains(source))
            return Collections.emptyList();

        List<MatchingMovies> path = new LinkedList<>();
        Set<MatchingMovies> neigbroos = new HashSet<>();

        Stack<MatchingMovies> stack = new Stack<>();
        stack.add(source);

        while (!stack.isEmpty()) {
            MatchingMovies v = stack.pop();

            visited.add(v);
            processVertex(v, path);

            for (MatchingMovies neighbor : graph.getNeighbors(v)) {
                if (!visited.contains(neighbor) && !neigbroos.contains(neighbor)) {
                    neigbroos.add(neighbor);
                    stack.add(neighbor);
                }
            }
        }
        return path;
    }


    public List<MatchingMovies> getPathFrom(MatchingMovies source) {
        return searchIterative(source);
    }
}

