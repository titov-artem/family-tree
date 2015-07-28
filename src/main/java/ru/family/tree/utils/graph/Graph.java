package ru.family.tree.utils.graph;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;
import java.util.Set;

/**
 * @author scorpion@yandex-team on 13.04.15.
 */
public class Graph<T> {
    private final ListMultimap<Vertex<T>, Edge<T>> graph;

    public Graph() {
        graph = ArrayListMultimap.create();
    }

    public void addEdge(final Edge<T> e) {
        graph.put(e.getStart(), e);
    }

    public Set<Vertex<T>> getVertexes() {
        return graph.keySet();
    }

    public List<Edge<T>> getEdges(final Vertex<T> v) {
        return graph.get(v);
    }

    public boolean isEmpty() {
        return graph.isEmpty();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Graph<?> graph1 = (Graph<?>) o;

        return graph.equals(graph1.graph);

    }

    @Override
    public int hashCode() {
        return graph.hashCode();
    }

    @Override
    public String toString() {
        return "Graph{" +
                "graph=" + graph +
                '}';
    }
}
