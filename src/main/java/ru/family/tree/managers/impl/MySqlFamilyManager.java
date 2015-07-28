package ru.family.tree.managers.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Required;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author scorpion@yandex-team on 31.03.15.
 */
public class MySqlFamilyManager implements FamilyManager {

    private static final RowMapper<FamilyMember> FAMILY_MEMBER_ROW_MAPPER = new RowMapper<FamilyMember>() {
        @Override
        public FamilyMember mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new FamilyMember(rs.getLong("id"), rs.getLong("person_id"));
        }
    };
    private static final RowMapper<SimpleFamilyRelation> FAMILY_RELATIONS_ROW_MAPPER = new RowMapper<SimpleFamilyRelation>() {
        @Override
        public SimpleFamilyRelation mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new SimpleFamilyRelation(rs.getLong("parent_id"), rs.getLong("child_id"));
        }
    };
    public static final Object[] EMPTY_PARAMS = new Object[0];

    private PersonManager personManager;
    private JdbcOperations jdbcOperations;
    private TransactionOperations transactionOperations;

    @Override
    public Family createFamilyTree(final Family Family) {
        return transactionOperations.execute(status -> {
            final long familyId = createFamily();
            final Multimap<Person, Person> parentsToChildren = Family.getParentsToChildren();
            for (final Entry<Person, Collection<Person>> entry : parentsToChildren.asMap().entrySet()) {
                addChildrenInternal(
                        familyId,
                        entry.getKey().getId(),
                        entry.getValue().stream().map(Person::getId).collect(Collectors.toList())
                );
            }
            return loadInternal(familyId);
        });
    }

    @Override
    public Family createFamily(final long ownerId) {
        return transactionOperations.execute(status -> {
            final long familyId = createFamilyInternal(ownerId);
            return loadInternal(familyId);
        });

    }

    @Override
    public Family createFamily(final Family family) {
        return null;
    }

    @Override
    public Family load(final long familyId) {
        return loadInternal(familyId);
    }

    @Override
    public Family loadByPerson(final long personId) {
        final Long familyId = loadFamilyId(personId);
        return loadInternal(familyId);
    }

    @Override
    public void addSpouse(final long personId, final long spouseId) {

    }

    @Override
    public List<Long> loadChildren(final long personId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addChildren(final long personId, final Collection<Long> childrenIds) {
        final Long familyId = loadFamilyId(personId);
        addChildrenInternal(familyId, personId, childrenIds);
    }

    private void addChildrenInternal(final Long familyId, final long personId, final Collection<Long> childrenIds) {
        final Iterator<Long> iterator = childrenIds.iterator();
        transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
                jdbcOperations.batchUpdate("INSERT INTO family_relations (family_id, parent_id, child_id) VALUES (?, ?, ?)",
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

    @Override
    public void removeChildren(final long personId, final Collection<Long> childrenIds) {
        final Long familyId = loadFamilyId(personId);
        final Iterator<Long> iterator = childrenIds.iterator();
        transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
                jdbcOperations.batchUpdate("DELETE FROM family_relations WHERE family_id = ? AND parent_id = ? AND child_id = ?",
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

    @Override
    public List<Long> loadParents(final long personId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addParents(final long personId, final Collection<Long> parentIds) {
        final Long familyId = loadFamilyId(personId);
        final Iterator<Long> iterator = parentIds.iterator();
        transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
                jdbcOperations.batchUpdate("INSERT INTO family_relations (family_id, parent_id, child_id) VALUES (?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                                final Long parentId = iterator.next();
                                ps.setLong(1, familyId);
                                ps.setLong(2, parentId);
                                ps.setLong(3, personId);
                            }

                            @Override
                            public int getBatchSize() {
                                return parentIds.size();
                            }
                        }
                );
            }
        });

    }

    @Override
    public void removeParents(final long personId, final Collection<Long> parentIds) {
        final Long familyId = loadFamilyId(personId);
        final Iterator<Long> iterator = parentIds.iterator();
        transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
                jdbcOperations.batchUpdate("DELETE FROM family_relations WHERE family_id = ? AND parent_id = ? AND child_id = ?",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                                final Long parentId = iterator.next();
                                ps.setLong(1, familyId);
                                ps.setLong(2, parentId);
                                ps.setLong(3, personId);
                            }

                            @Override
                            public int getBatchSize() {
                                return parentIds.size();
                            }
                        }
                );
            }
        });
    }

    private long createFamilyInternal(final long ownerId) {
        final PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                "INSERT INTO family VALUES (owner_id)", Types.BIGINT);
        factory.setReturnGeneratedKeys(true);
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(
                factory.newPreparedStatementCreator(new Object[]{ownerId}),
                keyHolder
        );
        return keyHolder.getKey().longValue();
    }

    private Long loadFamilyId(final long personId) {
        return jdbcOperations.queryForObject(
                "SELECT * FROM family_relations WHERE parent_id = ? OR child_id = ? LIMIT 1",
                Long.class,
                personId, personId
        );
    }

    private Family loadInternal(final long familyId) {
        final List<SimpleFamilyRelation> simpleFamilyRelations = jdbcOperations.query("SELECT * FROM family_relations WHERE family_id = ?",
                FAMILY_RELATIONS_ROW_MAPPER, familyId);

        final Set<Long> personIds = Sets.newHashSet();
        personIds.addAll(simpleFamilyRelations.stream().map(r -> r.parentId).collect(Collectors.toList()));
        personIds.addAll(simpleFamilyRelations.stream().map(r -> r.childId).collect(Collectors.toList()));

        final ImmutableMap<Long, Person> personById = Maps.uniqueIndex(personManager.load(personIds), Person::getId);
        final List<SimpleFamilyRelation> sortedRelations = FamilyRelationsHelper.sort(simpleFamilyRelations);

        final Builder builder = new Builder(familyId);
        for (final SimpleFamilyRelation relation : sortedRelations) {
            builder.addChild(personById.get(relation.parentId), personById.get(relation.childId));
        }
        return builder.build();
    }

    static final class FamilyMember {
        public final long familyId;
        public final long personId;

        FamilyMember(final long familyId, final long personId) {
            this.familyId = familyId;
            this.personId = personId;
        }
    }

    static final class SimpleFamilyRelation {
        public final long parentId;
        public final long childId;

        SimpleFamilyRelation(final long parentId, final long childId) {
            this.parentId = parentId;
            this.childId = childId;
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
