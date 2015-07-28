package ru.family.tree.controllers.impl;

import ru.family.tree.controllers.FamilyTreeController;
import ru.family.tree.model.FamilyTree;
import ru.family.tree.model.FamilyTree.Builder;
import ru.family.tree.model.Relation;

/**
 * @author scorpion@yandex-team on 17.04.15.
 */
public class FamilyTreeControllerImpl implements FamilyTreeController {

    @Override
    public FamilyTree createFamily(final long personId1, final long personId2, final Relation relation) {
        final Builder builder = new Builder(FamilyTree.FAKE_ID);
        return null;  // todo implement me
    }
}
