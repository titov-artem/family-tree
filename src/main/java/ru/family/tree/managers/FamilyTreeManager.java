package ru.family.tree.managers;

import com.google.common.annotations.Beta;
import ru.family.tree.model.FamilyTree;
import ru.family.tree.model.Person;

import java.util.Collection;
import java.util.List;

/**
 * @author scorpion@yandex-team on 31.03.15.
 */
public interface FamilyTreeManager {

    FamilyTree createFamilyTree(long parentId, long childId);

    FamilyTree createFamilyTree(FamilyTree familyTree);

    FamilyTree load(long familyId);

    FamilyTree loadByPerson(long personId);

    @Beta
    List<Long> loadChildren(long personId);

    void addChildren(long personId, Collection<Long> childrenIds);

    void removeChildren(long personId, Collection<Long> childrenIds);

    @Beta
    List<Long> loadParents(long personId);

    void addParents(long personId, Collection<Long> parentIds);

    void removeParents(long personId, Collection<Long> parentIds);

}
