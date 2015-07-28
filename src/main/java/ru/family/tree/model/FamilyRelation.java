package ru.family.tree.model;

/**
 * @author scorpion@yandex-team on 17.04.15.
 */
public final class FamilyRelation {
    private final Person parent;
    private final Person child;

    public FamilyRelation(final Person parent, final Person child) {
        this.parent = parent;
        this.child = child;
    }

    public Person getParent() {
        return parent;
    }

    public Person getChild() {
        return child;
    }
}
