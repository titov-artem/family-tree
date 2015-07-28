package ru.family.tree.utils.graph;

/**
 * @author scorpion@yandex-team on 13.04.15.
 */
public class Vertex<T> {
    private final T value;

    public Vertex(final T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Vertex<?> vertex = (Vertex<?>) o;

        return value.equals(vertex.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
