package ru.family.tree.managers;

import ru.family.tree.model.Person;

import java.util.List;
import java.util.Optional;

/**
 * @author scorpion@yandex-team on 31.03.15.
 */
public interface PersonManager {

    List<Person> loadAll();

    Optional<Person> load(long id);

    List<Person> load(Iterable<Long> ids);

    Person create(Person person);

    Person update(Person person);

    void delete(long id);

}
