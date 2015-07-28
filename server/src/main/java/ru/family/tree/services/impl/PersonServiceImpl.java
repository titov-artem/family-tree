package ru.family.tree.services.impl;

import org.springframework.beans.factory.annotation.Required;
import ru.family.tree.managers.PersonManager;
import ru.family.tree.model.Person;
import ru.family.tree.services.PersonService;
import ru.family.tree.services.dto.PersonDto;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author scorpion@yandex-team on 14.04.15.
 */
public class PersonServiceImpl implements PersonService {

    private PersonManager personManager;

    @Override
    public List<PersonDto> loadAll() {
        return personManager.loadAll().stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDto load(final long id) {
        final Optional<Person> maybePerson = personManager.load(id);
        if (maybePerson.isPresent()) {
            return new PersonDto(maybePerson.get());
        } else {
            throw new NotFoundException("No person with id " + id);
        }
    }

    @Override
    public PersonDto save(final PersonDto person) {
        return new PersonDto(personManager.create(Person.newPerson(
                person.getName(),
                person.getSurname(),
                person.getDescription(),
                person.getMainPhoto()
        )));
    }

    @Override
    public PersonDto update(final PersonDto person) {
        return new PersonDto(personManager.update(new Person(
                person.getId(),
                person.getName(),
                person.getSurname(),
                person.getDescription(),
                person.getMainPhoto()
        )));

    }

    @Override
    public void delete(final long id) {
        personManager.delete(id);
    }

    /*
    Setters
     */
    @Required
    public void setPersonManager(final PersonManager personManager) {
        this.personManager = personManager;
    }
}
