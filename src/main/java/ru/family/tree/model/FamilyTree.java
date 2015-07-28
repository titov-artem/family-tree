package ru.family.tree.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author scorpion@yandex-team on 30.03.15.
 */
public class FamilyTree {
    public static final long FAKE_ID = -1;

    private final long id;
    /**
     * Root is a node in family tree which linked to the person for whom this tree was built
     */
    private final Node root;
    private final Map<Person, Node> personToNode;

    private FamilyTree(final long id, final Node root, final Map<Person, Node> personToNode) {
        this.id = id;
        this.root = root;
        this.personToNode = personToNode;
    }

    public long getId() {
        return id;
    }

    public Person getOwner() {
        return root.person;
    }

    /**
     * @return topologically sorted list of family relations
     */
    public List<FamilyRelation> getRelations() {
        //todo
        final List<FamilyRelation> relations = Lists.newArrayList();
        for (final Node node : personToNode.values()) {
            for (final Node child : node.children) {
                relations.add(new FamilyRelation(node.person, child.person));
            }
        }
        return relations;
    }

    public Multimap<Person, Person> getParentsToChildren() {
        final Multimap<Person, Person> out = HashMultimap.create();
        for (final Node node : personToNode.values()) {
            for (final Node child : node.children) {
                out.put(node.person, child.person);
            }
        }
        return out;
    }



    /**
     * Describe node in family tree. One node is one person who can have children and parents
     */
    private static class Node {
        private final Person person;
        private final List<Node> children;
        private final List<Node> parents;

        private Node(final Person person, final List<Node> children, final List<Node> parents) {
            this.person = person;
            this.children = children;
            this.parents = parents;
        }

        private Node(final Person person) {
            this(person, Collections.<Node>emptyList(), Collections.<Node>emptyList());
        }
    }

    public static final class Builder {
        private final long id;
        private Node root;
        private final Map<Person, Node> personToNode = Maps.newHashMap();

        public Builder(final long id, final Person person) {
            this.id = id;
            this.root = summonNode(person, personToNode);
        }

        public Builder(final long id) {
            this.id = id;
        }

        public void addRelation(final Person parent, final Person child) {
            final Node parentNode = summonNode(parent, personToNode);
            final Node childNode = summonNode(child, personToNode);
            parentNode.children.add(childNode);
            childNode.parents.add(parentNode);
        }

        private static Node summonNode(final Person person, final Map<Person, Node> personToNode) {
            if (personToNode.containsKey(person)) {
                return personToNode.get(person);
            }

            final Node node = new Node(person);
            personToNode.put(person, node);
            return node;
        }

        public FamilyTree build() {
            return new FamilyTree(id, root, personToNode);
        }
    }

}
