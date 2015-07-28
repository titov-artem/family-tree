package ru.family.tree.utils.graph;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * @author scorpion@yandex-team on 13.04.15.
 */
public class TopologicalSort {

    private TopologicalSort() {
    }

    public static <T> List<Vertex<T>> sort(final Graph<T> source) {
        if (source.isEmpty()) {
            return Lists.newArrayList();
        }

        final Stack<Vertex<T>> stack = new Stack<>();
        final Set<Vertex<T>> greyVertexes = Sets.newHashSet();
        gfs(source.getVertexes().iterator().next(), source, greyVertexes, stack);
        final List<Vertex<T>> out = Lists.newArrayListWithExpectedSize(stack.size());
        while (!stack.isEmpty()) {
            out.add(stack.pop());
        }
        return out;
    }

    private static <T> void gfs(final Vertex<T> start,
                                final Graph<T> g,
                                final Set<Vertex<T>> greyVertexes,
                                final Stack<Vertex<T>> stack) {
        greyVertexes.add(start);
        for (final Edge<T> e : g.getEdges(start)) {
            if (greyVertexes.contains(e.getEnd())) {
                continue;
            }
            gfs(e.getEnd(), g, greyVertexes, stack);
        }
        stack.add(start);
    }

}
