package ru.family.tree.controllers;

import ru.family.tree.model.FamilyTree;
import ru.family.tree.model.Relation;

/**
 * @author scorpion@yandex-team on 17.04.15.
 */
public interface FamilyTreeController {

    FamilyTree createFamily(long personId1, long personId2, Relation relation);
}
