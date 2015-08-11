package ru.family.tree.services.dto;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Maps;
import ru.family.tree.model.Family;
import ru.family.tree.model.Person;
import ru.family.tree.utils.graph.Edge;
import ru.family.tree.utils.graph.Graph;
import ru.family.tree.utils.graph.TopologicalSort;
import ru.family.tree.utils.graph.Vertex;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class FamilyHelper {

    private static final Integer INIT_GENERATION_ID = 0;

    public FamilyDto toDto(final Family family) {
        throw new UnsupportedOperationException();
    }

    private List<GenerationDto> toGenerations(final Family family) {
        ImmutableSetMultimap<Person, Person> parentsToChildren = family.getParentsToChildren();
        Graph<Person> g = new Graph<>();
        for (final Map.Entry<Person, Person> entry : parentsToChildren.entries()) {
            final Person parent = entry.getKey();
            final Person child = entry.getValue();
            g.addEdge(new Edge<>(new Vertex<>(parent), new Vertex<>(child)));
        }
        List<Vertex<Person>> sortedPersons = TopologicalSort.sort(g);
        Map<Person, Integer> personToGeneration = Maps.newHashMap();
        personToGeneration.put(sortedPersons.get(0).getValue(), INIT_GENERATION_ID);
        for (final Vertex<Person> person : sortedPersons) {
            Person parent = person.getValue();
            final int parentGeneration = personToGeneration.get(parent);
            final int childrenGeneration = parentGeneration - 1;
            ImmutableSet<Person> children = parentsToChildren.get(parent);
            for (final Person child : children) {
                @Nullable Integer curChildGen = personToGeneration.get(child);
                personToGeneration.put(
                        child,
                        curChildGen == null ? childrenGeneration : Math.min(curChildGen, childrenGeneration)
                );
            }
        }
        throw new UnsupportedOperationException();
    }

    private static final class GenerationBuilder {}
}
