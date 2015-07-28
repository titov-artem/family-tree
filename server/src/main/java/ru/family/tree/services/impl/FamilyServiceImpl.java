package ru.family.tree.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import ru.family.tree.managers.FamilyManager;
import ru.family.tree.model.Family;
import ru.family.tree.services.FamilyService;
import ru.family.tree.services.dto.FamilyDto;
import ru.family.tree.services.dto.RelationType;

import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.Optional;

/**
 * @author scorpion@yandex-team on 17.04.15.
 */
public class FamilyServiceImpl implements FamilyService {
    private static final Logger log = LoggerFactory.getLogger(FamilyServiceImpl.class);

    private FamilyManager familyManager;

    @Override
    public FamilyDto create(final long ownerId) {
        return new FamilyDto(familyManager.createFamily(ownerId));
    }

    @Override
    public FamilyDto load(final long familyId) {
        log.debug("Loading family {}", familyId);
        final Optional<Family> maybeFamily = familyManager.load(familyId);
        if (!maybeFamily.isPresent()) {
            throw new NotFoundException("No family with id " + familyId);
        }
        return new FamilyDto(maybeFamily.get());
    }

    @Override
    public void remove(final long familyId) {
        familyManager.removeFamily(familyId);
    }

    @Override
    public void addRelation(final long familyId, final long person1, final long person2, final RelationType relationType) {
        switch (relationType) {
            case PARENT_CHILD:
                addChild(familyId, person1, person2);
                break;
            case SPOUSES:
                addSpouse(familyId, person1, person2);
                break;
            default:
                throw new IllegalArgumentException("Unknown relation type: " + relationType);
        }
    }

    @Override
    public void addSpouse(final long familyId, final long spouse1, final long spouse2) {
        familyManager.addSpouse(familyId, spouse1, spouse2);
    }

    @Override
    public void removeSpouse(final long familyId, final long spouse1, final long spouse2) {
        familyManager.removeSpouse(familyId, spouse1, spouse2);
    }

    @Override
    public void addChild(final long familyId, final long parentId, final long childId) {
        familyManager.addChildren(familyId, parentId, Collections.singleton(childId));
    }

    @Override
    public void removeChild(final long familyId, final long parentId, final long childId) {
        familyManager.removeChildren(familyId, parentId, Collections.singleton(childId));
    }

    /*
    Setters
     */
    @Required
    public void setFamilyManager(final FamilyManager familyManager) {
        this.familyManager = familyManager;
    }
}
