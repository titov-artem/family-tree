package ru.family.tree.services.impl;

import org.springframework.beans.factory.annotation.Required;
import ru.family.tree.managers.FamilyManager;
import ru.family.tree.managers.FamilyTreeManager;
import ru.family.tree.model.Family;
import ru.family.tree.services.FamilyService;
import ru.family.tree.services.dto.FamilyDto;
import ru.family.tree.services.dto.FamilyTreeDto;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author scorpion@yandex-team on 17.04.15.
 */
public class FamilyServiceImpl implements FamilyService {

    private FamilyManager familyManager;

    @Override
    public FamilyDto create(@QueryParam("owner") final long ownerId) {
        return new FamilyDto(familyManager.createFamily(ownerId));
    }

    @Override
    public void addChild(@QueryParam("parentId") final long parentId, @QueryParam("childId") final long childId) {
        familyManager.addChildren(parentId, Collections.singleton(childId));
    }

    @Override
    public void addSpouse(@QueryParam("spouse1") final long spouse1, @QueryParam("spouse2") final long spouse2) {
        familyManager.addSpouse(spouse1, spouse2);
    }

    @Override
    public List<FamilyDto> loadAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FamilyDto load(@PathParam("id") final long id) {
        final Optional<Family> maybeFamily = familyManager.load(id);
        if (maybeFamily.isPresent())
            return new FamilyDto(maybeFamily.get());
        else
            throw new NotFoundException("No family with id " + id);
    }

    /*
    Setters
     */
    @Required
    public void setFamilyManager(final FamilyManager familyManager) {
        this.familyManager = familyManager;
    }
}
