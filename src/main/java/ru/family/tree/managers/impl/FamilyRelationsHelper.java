package ru.family.tree.managers.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import ru.family.tree.managers.impl.MySqlFamilyTreeManager.SimpleFamilyRelation;
import ru.family.tree.utils.graph.Edge;
import ru.family.tree.utils.graph.Graph;
import ru.family.tree.utils.graph.TopologicalSort;
import ru.family.tree.utils.graph.Vertex;

import java.util.List;

/**
 * @author scorpion@yandex-team on 13.04.15.
 */
public class FamilyRelationsHelper {

    private FamilyRelationsHelper() {
    }

    public static List<SimpleFamilyRelation> sort(final Iterable<SimpleFamilyRelation> familyRelations) {
        final Graph<Long> g = new Graph<>();
        final SetMultimap<Long, Long> personToChildren = HashMultimap.create();
        for (final SimpleFamilyRelation relation : familyRelations) {
            g.addEdge(new Edge<>(new Vertex<>(relation.parentId), new Vertex<>(relation.childId)));
            personToChildren.put(relation.parentId, relation.childId);
        }
        final List<Vertex<Long>> orderedPersons = TopologicalSort.sort(g);
        final List<SimpleFamilyRelation> out = Lists.newArrayList();
        for (final Vertex<Long> person : orderedPersons) {
            for (final Long child : personToChildren.get(person.getValue())) {
                out.add(new SimpleFamilyRelation(person.getValue(), child));
            }
        }
        return out;
    }

}
