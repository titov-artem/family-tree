package ru.family.tree.utils.graph;

/**
 * @author scorpion@yandex-team on 13.04.15.
 */
public class Edge<T> {
    private final Vertex<T> start;
    private final Vertex<T> end;

    public Edge(final Vertex<T> start, final Vertex<T> end) {
        this.start = start;
        this.end = end;
    }

    public Vertex<T> getStart() {
        return start;
    }

    public Vertex<T> getEnd() {
        return end;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Edge<?> edge = (Edge<?>) o;

        if (!start.equals(edge.start)) {
            return false;
        }
        return end.equals(edge.end);

    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "[" + start + " -> " + end + ']';
    }
}
