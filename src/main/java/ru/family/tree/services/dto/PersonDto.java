package ru.family.tree.services.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.family.tree.model.Person;

/**
 * @author scorpion@yandex-team on 31.03.15.
 */
public class PersonDto {
    private final long id;

    private final String name;
    private final String surname;

    private final String description;

    private final String mainPhoto;

    @JsonCreator
    public PersonDto(@JsonProperty("id") final long id,
                     @JsonProperty("name") final String name,
                     @JsonProperty("surname") final String surname,
                     @JsonProperty("description") final String description,
                     @JsonProperty("mainPhoto") final String mainPhoto) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.mainPhoto = mainPhoto;
    }

    public PersonDto(final Person person) {
        this(person.getId(), person.getName(), person.getSurname(), person.getDescription(), person.getMainPhoto());
    }

    @JsonGetter
    public long getId() {
        return id;
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    @JsonGetter
    public String getSurname() {
        return surname;
    }

    @JsonGetter
    public String getDescription() {
        return description;
    }

    @JsonGetter
    public String getMainPhoto() {
        return mainPhoto;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PersonDto personDto = (PersonDto) o;

        if (id != personDto.id) return false;
        if (!name.equals(personDto.name)) return false;
        if (!surname.equals(personDto.surname)) return false;
        if (!description.equals(personDto.description)) return false;
        return mainPhoto.equals(personDto.mainPhoto);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ id >>> 32);
        result = 31 * result + name.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + mainPhoto.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PersonDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", description='" + description + '\'' +
                ", mainPhoto='" + mainPhoto + '\'' +
                '}';
    }
}
