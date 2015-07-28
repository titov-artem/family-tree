package ru.family.tree.services.dto;

import java.util.List;
import java.util.Map;

/**
 * @author scorpion@yandex-team.ru on 04.07.15.
 */
public class GenerationDto {
    private final List<PersonDto> persons;

    private Map<PersonDto, List<PersonDto>> spouses;

    public GenerationDto(final List<PersonDto> persons) {
        this.persons = persons;
    }
}
