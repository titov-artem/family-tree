package ru.family.tree.services.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import ru.family.tree.model.Family;

/**
 * @author titov.artem.u@yandex.com on 25.06.15.
 */
public class FamilyDto {

    private final Family family;

    public FamilyDto(final Family family) {
        this.family = family;
    }

    @JsonGetter
    public long getId() {
        return family.getId();
    }

    @JsonGetter
    public PersonDto getOwner() {
        return new PersonDto(family.getOwner());
    }

}
