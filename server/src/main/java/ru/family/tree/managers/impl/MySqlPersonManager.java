package ru.family.tree.managers.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.family.tree.managers.PersonManager;
import ru.family.tree.model.Person;
import ru.family.tree.utils.db.DbUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

/**
 * @author scorpion@yandex-team on 31.03.15.
 */
public class MySqlPersonManager implements PersonManager {

    private static final RowMapper<Person> ROW_MAPPER = new RowMapper<Person>() {
        @Override
        public Person mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Person(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("description"),
                    rs.getString("main_photo")
            );
        }
    };

    private JdbcOperations jdbcOperations;

    @Override
    public List<Person> loadAll() {
        return jdbcOperations.query("SELECT * FROM person", ROW_MAPPER);
    }

    @Override
    public Optional<Person> load(final long id) {
        try {
            return Optional.of(jdbcOperations.queryForObject("SELECT * FROM person WHERE id = ?", ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException ignore) {
            return Optional.empty();
        }
    }

    @Override
    public List<Person> load(final Iterable<Long> ids) {
        final List<Person> out = Lists.newArrayList();
        for (final List<Long> chunk : Iterables.partition(ids, 1000)) {
            out.addAll(jdbcOperations.query(
                    DbUtils.generatePlaceholders("SELECT * FROM person WHERE id IN (#)", chunk.size()),
                    ROW_MAPPER,
                    chunk.toArray()
            ));
        }
        return out;
    }

    @Override
    public Person create(final Person person) {
        final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "INSERT INTO person (name, surname, description, main_photo) VALUES (?, ?, ?, ?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR
        );
        pscf.setReturnGeneratedKeys(true);
        final PreparedStatementCreator psc = pscf.newPreparedStatementCreator(new Object[]{
                person.getName(), person.getSurname(), person.getDescription(), person.getMainPhoto()
        });
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        return new Person(
                keyHolder.getKey().longValue(),
                person.getName(), person.getSurname(), person.getDescription(), person.getMainPhoto()
        );
    }

    @Override
    public Person update(final Person person) {
        jdbcOperations.update(
                "UPDATE person SET name = ?, surname = ?, description = ?, main_photo = ? WHERE id = ?",
                person.getName(), person.getSurname(), person.getDescription(), person.getMainPhoto(), person.getId()
        );
        return person;
    }

    @Override
    public void delete(final long id) {
        jdbcOperations.update("DELETE FROM person WHERE id = ?", id);
    }

    /*
    Setters
     */
    @Required
    public void setJdbcOperations(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
}
