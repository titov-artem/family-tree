package ru.family.tree.managers.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionOperations;
import ru.family.tree.managers.FamilyManager;
import ru.family.tree.managers.PersonManager;
import ru.family.tree.model.Family;
import ru.family.tree.model.Family.Builder;
import ru.family.tree.model.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author scorpion@yandex-team on 31.03.15.
 */
public class MySqlFamilyManager implements FamilyManager {

    private static final RowMapper<ParentChildRelation> PARENT_CHILD_RELATIONS_ROW_MAPPER = new RowMapper<ParentChildRelation>() {
        @SuppressWarnings("ClassEscapesDefinedScope")
        @Override
        public ParentChildRelation mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new ParentChildRelation(rs.getLong("parent_id"), rs.getLong("child_id"));
        }
    };
    private static final RowMapper<SpouseRelation> SPOUSE_RELATIONS_ROW_MAPPER = new RowMapper<SpouseRelation>() {
        @SuppressWarnings("ClassEscapesDefinedScope")
        @Override
        public SpouseRelation mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new SpouseRelation(rs.getLong("spouse1"), rs.getLong("spouse2"));
        }
    };

    private PersonManager personManager;
    private JdbcOperations jdbcOperations;
    private TransactionOperations transactionOperations;

    @Override
    public Family createFamily(final long ownerId) {
        return transactionOperations.execute(status -> {
            final long familyId = createFamilyInternal(ownerId);
            return loadInternal(familyId);
        });

    }

    @Override
    public Optional<Family> load(final long familyId) {
        try {
            return Optional.of(loadInternal(familyId));
        } catch (EmptyResultDataAccessException ignore) {
            return Optional.empty();
        }
    }

    @Override
    public void removeFamily(final long familyId) {
        transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus status) {
                jdbcOperations.update("DELETE FROM family_spouses WHERE family_id = ?", familyId);
                jdbcOperations.update("DELETE FROM family_child WHERE family_id = ?", familyId);
                jdbcOperations.update("DELETE FROM family WHERE id = ?", familyId);
            }
        });
    }

    @Override
    public void addSpouse(final long familyId, final long personId, final long spouseId) {
        addSpousesInternal(familyId, Collections.singleton(new SpouseRelation(personId, spouseId)));
    }

    @Override
    public void removeSpouse(final long familyId, final long personId, final long spouseId) {
        removeSpousesInternal(familyId, Collections.singleton(new SpouseRelation(personId, spouseId)));
    }

    @Override
    public void addChildren(final long familyId, final long parentId, final Collection<Long> childrenIds) {
        addChildrenInternal(familyId, parentId, childrenIds);
    }

    @Override
    public void removeChildren(final long familyId, final long personId, final Collection<Long> childrenIds) {
        removeChildrenInternal(familyId, personId, childrenIds);
    }

    private void addSpousesInternal(final long familyId, final Collection<SpouseRelation> spouses) {
        final Iterator<SpouseRelation> iterator = spouses.iterator();
        transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus status) {
                jdbcOperations.batchUpdate("INSERT IGNORE INTO family_spouses (family_id, spouse1, spouse2) VALUES (?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                                final SpouseRelation spouses = iterator.next();
                                ps.setLong(1, familyId);
                                ps.setLong(2, spouses.spouse1);
                                ps.setLong(3, spouses.spouse2);
                            }

                            @Override
                            public int getBatchSize() {
                                return spouses.size();
                            }
                        });
            }
        });
    }

    private void removeSpousesInternal(final long familyId, final Collection<SpouseRelation> spouses) {
        final Iterator<SpouseRelation> iterator = spouses.iterator();
        transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus status) {
                jdbcOperations.batchUpdate("DELETE FROM family_spouses WHERE family_id = ? AND spouse1 = ? AND spouse2 = ?",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                                final SpouseRelation spouses = iterator.next();
                                ps.setLong(1, familyId);
                                ps.setLong(2, spouses.spouse1);
                                ps.setLong(3, spouses.spouse2);
                            }

                            @Override
                            public int getBatchSize() {
                                return spouses.size();
                            }
                        });
            }
        });
    }

    private void addChildrenInternal(final Long familyId, final long personId, final Collection<Long> childrenIds) {
        final Iterator<Long> iterator = childrenIds.iterator();
        transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
                jdbcOperations.batchUpdate("INSERT INTO family_child (family_id, parent_id, child_id) VALUES (?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                                final Long childId = iterator.next();
                                ps.setLong(1, familyId);
                                ps.setLong(2, personId);
                                ps.setLong(3, childId);
                            }

                            @Override
                            public int getBatchSize() {
                                return childrenIds.size();
                            }
                        }
                );
            }
        });
    }

    private void removeChildrenInternal(final Long familyId, final long personId, final Collection<Long> childrenIds) {
        final Iterator<Long> iterator = childrenIds.iterator();
        transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
                jdbcOperations.batchUpdate("DELETE FROM family_child WHERE family_id = ? AND parent_id = ? AND child_id = ?",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                                final Long childId = iterator.next();
                                ps.setLong(1, familyId);
                                ps.setLong(2, personId);
                                ps.setLong(3, childId);
                            }

                            @Override
                            public int getBatchSize() {
                                return childrenIds.size();
                            }
                        }
                );
            }
        });
    }

    private long createFamilyInternal(final long ownerId) {
        final PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                "INSERT INTO family (owner_id) VALUES (?)", Types.BIGINT);
        factory.setReturnGeneratedKeys(true);
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(
                factory.newPreparedStatementCreator(new Object[]{ownerId}),
                keyHolder
        );
        return keyHolder.getKey().longValue();
    }

    /**
     * Load family by id or throw exception if now such family found
     *
     * @param familyId
     * @return
     * @throws EmptyResultDataAccessException
     */
    private Family loadInternal(final long familyId) throws EmptyResultDataAccessException {
        final Long ownerId = jdbcOperations.queryForObject("SELECT owner_id FROM family WHERE id = ?", Long.class, familyId);
        final List<ParentChildRelation> parentChildRelations = jdbcOperations.query("SELECT * FROM family_child WHERE family_id = ?",
                PARENT_CHILD_RELATIONS_ROW_MAPPER, familyId);
        final List<SpouseRelation> spouseRelations = jdbcOperations.query("SELECT * FROM family_spouses WHERE family_id = ?",
                SPOUSE_RELATIONS_ROW_MAPPER, familyId);

        final SetConsumer<Long> personIdsConsumer = new SetConsumer<>();
        parentChildRelations.stream().map(r -> r.parentId).forEach(personIdsConsumer);
        parentChildRelations.stream().map(r -> r.childId).forEach(personIdsConsumer);
        spouseRelations.stream().map(r -> r.spouse1).forEach(personIdsConsumer);
        spouseRelations.stream().map(r -> r.spouse2).forEach(personIdsConsumer);
        personIdsConsumer.accept(ownerId);

        final ImmutableMap<Long, Person> personById = Maps.uniqueIndex(personManager.load(personIdsConsumer.getResult()), Person::getId);

        final Builder builder = new Builder(familyId, personById.get(ownerId));
        for (final ParentChildRelation relation : parentChildRelations) {
            builder.addChild(personById.get(relation.parentId), personById.get(relation.childId));
        }
        for (final SpouseRelation spouseRelation : spouseRelations) {
            builder.addSpouses(personById.get(spouseRelation.spouse1), personById.get(spouseRelation.spouse2));
        }
        return builder.build();
    }

    private static class ParentChildRelation {
        public final long parentId;
        public final long childId;

        ParentChildRelation(final long parentId, final long childId) {
            this.parentId = parentId;
            this.childId = childId;
        }
    }

    private static class SpouseRelation {
        public final long spouse1;
        public final long spouse2;

        SpouseRelation(final long spouse1, final long spouse2) {
            this.spouse1 = Math.min(spouse1, spouse2);
            this.spouse2 = Math.max(spouse1, spouse2);
        }
    }

    private static class SetConsumer<T> implements Consumer<T> {
        private final Set<T> s = new HashSet<>();

        @Override
        public void accept(final T t) {
            s.add(t);
        }

        public Set<T> getResult() {
            return Collections.unmodifiableSet(s);
        }
    }

    /*
    Setters
     */
    @Required
    public void setPersonManager(final PersonManager personManager) {
        this.personManager = personManager;
    }

    @Required
    public void setJdbcOperations(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Required
    public void setTransactionOperations(final TransactionOperations transactionOperations) {
        this.transactionOperations = transactionOperations;
    }
}
