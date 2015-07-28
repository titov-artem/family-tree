package ru.family.tree.model;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

/**
 * @author scorpion@yandex-team.ru on 25.06.15.
 */
public class Family {

    private final long id;
    /**
     * Person who create this family
     */
    private final Person owner;
    /**
     * For each person in the family contains all it's children
     */
    private final ImmutableSetMultimap<Person, Person> parentsToChildren;
    /**
     * For each person in the family contains all its husbands and wives
     */
    private final ImmutableSetMultimap<Person, Person> spouses;

    public Family(final long id,
                  final Person owner,
                  final ImmutableSetMultimap<Person, Person> parentsToChildren,
                  final ImmutableSetMultimap<Person, Person> spouses) {
        this.id = id;
        this.owner = owner;
        this.parentsToChildren = parentsToChildren;
        this.spouses = spouses;
    }

    public ImmutableSetMultimap<Person, Person> getParentsToChildren() {
        return parentsToChildren;
    }

    public long getId() {
        return id;
    }

    public Person getOwner() {
        return owner;
    }

    public ImmutableSetMultimap<Person, Person> getSpouses() {
        return spouses;
    }

    public static final class Builder {
        private final long id;
        private final Person owner;
        private SetMultimap<Person, Person> parentsToChildren;
        private SetMultimap<Person, Person> spouses;

        public Builder(final long id, final Person owner) {
            this.id = id;
            this.owner = owner;
        }

        public void addChild(final Person parent, final Person child) {
            parentsToChildren.put(parent, child);
        }

        public void addSpouses(final Person spouse1, final Person spouse2) {
            spouses.put(spouse1, spouse2);
            spouses.put(spouse2, spouse1);
        }

        public Family build() {
            return new Family(
                    id,
                    owner,
                    ImmutableSetMultimap.copyOf(parentsToChildren),
                    ImmutableSetMultimap.copyOf(spouses)
            );
        }
    }
}
