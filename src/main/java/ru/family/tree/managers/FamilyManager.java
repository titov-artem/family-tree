package ru.family.tree.managers;

import com.google.common.annotations.Beta;
import ru.family.tree.model.Family;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author scorpion@yandex-team on 31.03.15.
 */
public interface FamilyManager {

    Family createFamily(long ownerId);

    Family createFamily(Family family);

    Optional<Family> load(long familyId);

    Optional<Family> loadByPerson(long personId);

    void addSpouse(long personId, long spouseId);

    @Beta
    List<Long> loadChildren(long personId);

    void addChildren(long personId, Collection<Long> childrenIds);

    void removeChildren(long personId, Collection<Long> childrenIds);

    @Beta
    List<Long> loadParents(long personId);

    void addParents(long personId, Collection<Long> parentIds);

    void removeParents(long personId, Collection<Long> parentIds);

}
