package ru.family.tree.model;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

/**
 * @author scorpion@yandex-team on 30.03.15.
 */
public class Person {
    public static final Person EMPTY = newPerson(null, null, "Currently unknown", null);
    public static final long FAKE_ID = -1;

    private final long id;

    private final String name;
    private final String surname;

    private final String description;

    private final String mainPhoto;

    // not used
    private final DateTime birthDate;
    private final DateTime deathDate;
    private final List<String> photos;

    public Person(final long id,
                  final String name,
                  final String surname,
                  final String description,
                  final String mainPhoto) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.mainPhoto = mainPhoto;

        this.birthDate = null;
        this.deathDate = null;
        this.photos = Collections.emptyList();
    }

    public static Person newPerson(final String name,
                                   final String surname,
                                   final String description,
                                   final String mainPhoto) {
        return new Person(FAKE_ID, name, surname, description, mainPhoto);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDescription() {
        return description;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Person person = (Person) o;

        if (id != person.id) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (surname != null ? !surname.equals(person.surname) : person.surname != null) return false;
        if (description != null ? !description.equals(person.description) : person.description != null) return false;
        return !(mainPhoto != null ? !mainPhoto.equals(person.mainPhoto) : person.mainPhoto != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ id >>> 32);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (mainPhoto != null ? mainPhoto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", description='" + description + '\'' +
                ", mainPhoto='" + mainPhoto + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                ", photos=" + photos +
                '}';
    }
}
