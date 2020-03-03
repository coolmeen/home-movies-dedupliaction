package com.homeass.deduplication.movies.matcher;

import com.homeass.deduplication.common.graph.DFS;
import com.homeass.deduplication.common.graph.Graph;
import com.homeass.deduplication.movies.entity.MatchingMovies;
import com.homeass.deduplication.movies.entity.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MoviesMatchingService {
    MatchingStrategy<Movie> matchingStrategy;

    @Autowired
    public MoviesMatchingService(MatchingStrategy<Movie> matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    public List<MatchingMovies> findMatches(List<Movie> movieList) {

        return matchingStrategy.findMatches(movieList)
                .stream()
                .distinct()
                .map(listOfMatchingMovies -> new MatchingMovies(listOfMatchingMovies, UUID.randomUUID().toString()))
                .collect(Collectors.toList());
    }

    public List<MatchingMovies> filterDuplicates(List<MatchingMovies> matchingList) {
        log.info("found matching : " + matchingList.size() + " elements");
        return matchingList.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<MatchingMovies> mergeMatchingListWithCommonElements(List<Movie> movieList, List<MatchingMovies> matchingList) {
        Graph graph = buildGraph(movieList, matchingList);
        log.info("finished building graph");
        return findAllTressInGraph(matchingList, graph);


    }

    private List<MatchingMovies> findAllTressInGraph(List<MatchingMovies> matchingList, Graph graph) {
        DFS dfs = new DFS(graph);
        return matchingList.stream()
                .map(dfs::getPathFrom)
                .filter(matchingMovies -> !matchingMovies.isEmpty())
                .map(allMatching -> allMatching
                        .stream()
                        .flatMap(matchingMovies -> matchingMovies.getListOfMatchingMovies().stream())
                        .collect(Collectors.toSet()))
                .map(listOfMatchingMovies -> new MatchingMovies(listOfMatchingMovies, UUID.randomUUID().toString()))
                .collect(Collectors.toList());
    }

    /*
    build graph where each matching list is a vertex and each edge to another list only if they have a common member
     */
    private Graph buildGraph(List<Movie> movieList, List<MatchingMovies> matchingList) {
        HashMap<Movie, Set<MatchingMovies>> invertedIndex = invertedIndexMovieToMatchingList(movieList, matchingList);
        Graph graph = new Graph(matchingList.size());
        matchingList.forEach(graph::addVertex);
        fillEdges(matchingList, invertedIndex, graph);
        return graph;
    }

    private void fillEdges(List<MatchingMovies> matchingList, HashMap<Movie, Set<MatchingMovies>> invertedIndex, Graph graph) {
        for (MatchingMovies first : matchingList) {
            Set<Movie> listOfMatchingMovies = first.getListOfMatchingMovies();
            for (Movie movie : listOfMatchingMovies) {
                Set<MatchingMovies> edges = invertedIndex.get(movie);
                if (edges.size() == 1)
                    continue;

                for (MatchingMovies matchingMovies : edges) {
                    graph.addEdge(first, matchingMovies);
                }
            }
        }
    }

    private HashMap<Movie, Set<MatchingMovies>> invertedIndexMovieToMatchingList(List<Movie> movieList, List<MatchingMovies> matchingList) {
        HashMap<Movie, Set<MatchingMovies>> movieSetHashMap = new HashMap<>(matchingList.size());
        movieList.forEach(movie -> movieSetHashMap.put(movie, new HashSet<>()));
        matchingList
                .forEach(matchingMovies -> matchingMovies.getListOfMatchingMovies()
                        .forEach(movie -> movieSetHashMap.get(movie)
                                .add(matchingMovies)));
        return movieSetHashMap;
    }
}
