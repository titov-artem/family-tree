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

    Optional<Family> load(long familyId);

    void removeFamily(long familyId);

    void addSpouse(long familyId, long personId, long spouseId);

    void removeSpouse(long familyId, long personId, long spouseId);

    void addChildren(long familyId, long parentId, Collection<Long> childrenIds);

    void removeChildren(long familyId, long personId, Collection<Long> childrenIds);

}
