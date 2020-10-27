package cn.javaer.snippets.spring.data.jooq.jdbc;

import cn.javaer.snippets.spring.data.jooq.IgnoreWithBatch;
import cn.javaer.snippets.spring.data.jooq.QueryStep;
import cn.javaer.snippets.spring.data.jooq.StepUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.Update;
import org.jooq.impl.DSL;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.core.convert.EntityRowMapper;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.util.Streamable;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link JooqJdbcRepository} interface.
 *
 * @author cn-src
 */
@Transactional(readOnly = true)
public class SimpleJooqJdbcRepository<T, ID> implements JooqJdbcRepository<T, ID> {

    private static final String AUDITOR_MUST_BE_NOT_NULL = "Auditor must be not null";

    private final JdbcAggregateOperations entityOperations;
    private final JdbcOperations jdbcOperations;
    private final JdbcConverter jdbcConverter;
    private final Class<T> entityClass;
    private final EntityRowMapper<T> entityRowMapper;
    private final RelationalPersistentEntity<T> persistentEntity;

    private final DSLContext dsl;
    private final Table<Record> table;
    private final Field<?>[] fieldsFromEntity;
    private final AuditorAware<?> auditorAware;

    private final AuditingHandler auditingHandler;

    public SimpleJooqJdbcRepository(final DSLContext dsl,
                                    final RelationalMappingContext context,
                                    final RelationalPersistentEntity<T> persistentEntity,
                                    final JdbcAggregateOperations entityOperations,
                                    final NamedParameterJdbcOperations jdbcOperations,
                                    final JdbcConverter jdbcConverter,
                                    final AuditorAware<?> auditorAware,
                                    final AuditingHandler auditingHandler) {
        this.entityOperations = entityOperations;
        this.jdbcConverter = jdbcConverter;
        this.jdbcOperations = jdbcOperations.getJdbcOperations();
        this.entityClass = persistentEntity.getType();
        this.entityRowMapper = new EntityRowMapper<>(persistentEntity, jdbcConverter);
        this.persistentEntity = persistentEntity;
        this.dsl = dsl;
        this.table = DSL.table(this.persistentEntity.getTableName().getReference());
        this.fieldsFromEntity = StepUtils.getFields(persistentEntity);
        this.auditorAware = auditorAware;
        this.auditingHandler = auditingHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public <S extends T> S save(final S instance) {
        return this.entityOperations.save(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public <S extends T> Iterable<S> saveAll(final Iterable<S> entities) {

        return Streamable.of(entities).stream()
            .map(this::save)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<T> findById(final ID id) {
        return Optional.ofNullable(this.entityOperations.findById(id, this.entityClass));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(final ID id) {
        return this.entityOperations.existsById(id, this.entityClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<T> findAll() {
        return this.entityOperations.findAll(this.entityClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<T> findAllById(final Iterable<ID> ids) {
        return this.entityOperations.findAllById(ids, this.entityClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        return this.entityOperations.count(this.entityClass);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteById(final ID id) {
        this.entityOperations.deleteById(id, this.entityClass);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void delete(final T instance) {
        this.entityOperations.delete(instance, this.entityClass);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public void deleteAll(final Iterable<? extends T> entities) {
        entities.forEach(it -> this.entityOperations.delete(it, (Class<T>) it.getClass()));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteAll() {
        this.entityOperations.deleteAll(this.entityClass);
    }

    @Transactional
    @Override
    public T insert(final T instance) {
        if (this.auditingHandler != null) {
            this.auditingHandler.markCreated(instance);
        }
        return this.entityOperations.insert(instance);
    }

    @Transactional
    @Override
    public <S extends T> int[] batchInsert(final Iterable<S> entities) {
        Assert.notNull(entities, "Entities must not be null!");

        final StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append(this.persistentEntity.getTableName().getReference());
        final StringJoiner columnJoiner = new StringJoiner(",", "(", ")");
        final StringJoiner valuesJoiner = new StringJoiner(",", "(", ")");

        int size = 0;
        for (final RelationalPersistentProperty property : this.persistentEntity) {
            if (property.isTransient() || property.isAnnotationPresent(IgnoreWithBatch.class)) {
                continue;
            }
            final String columnName = property.getColumnName().getReference();
            columnJoiner.add(columnName);
            valuesJoiner.add("?");
            size++;
        }

        sqlBuilder.append(columnJoiner.toString())
            .append(" VALUES ")
            .append(valuesJoiner.toString());

        final List<Object[]> batchValues = new ArrayList<>();
        for (final S entity : entities) {
            final PersistentPropertyAccessor<S> propertyAccessor =
                this.persistentEntity.getPropertyAccessor(entity);
            final List<Object> values = new ArrayList<>(size);
            for (final RelationalPersistentProperty property : this.persistentEntity) {
                if (property.isTransient() || property.isAnnotationPresent(IgnoreWithBatch.class)) {
                    continue;
                }
                final Object value = propertyAccessor.getProperty(property);
                values.add(value);
            }
            batchValues.add(this.conversion(values.toArray()));
        }
        return this.jdbcOperations.batchUpdate(sqlBuilder.toString(), batchValues);
    }

    @Transactional
    @Override
    public T update(final T instance) {
        return this.entityOperations.update(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<T> findAll(final Sort sort) {
        return this.entityOperations.findAll(this.entityClass, sort);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<T> findAll(final Pageable pageable) {
        return this.entityOperations.findAll(this.entityClass, pageable);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Optional<T> findOne(final Condition condition) {
        final Query query = this.dsl.select(this.fieldsFromEntity)
            .from(this.table)
            .where(condition)
            .limit(1);
        try {
            return Optional.of(this.jdbcOperations.queryForObject(query.getSQL(),
                query.getBindValues().toArray(),
                this.entityRowMapper));
        }
        catch (final EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll(final Condition condition) {
        final Query query = this.dsl.select(this.fieldsFromEntity)
            .from(this.table).where(condition);

        return this.jdbcOperations.query(query.getSQL(), query.getBindValues().toArray(),
            this.entityRowMapper);
    }

    @Override
    public List<T> findAll(final Condition condition, final Sort sort) {
        final Query query = StepUtils.sortStep(this.persistentEntity,
            this.dsl.select(this.fieldsFromEntity)
                .from(this.table).where(condition), sort);
        return this.jdbcOperations.query(query.getSQL(), query.getBindValues().toArray(),
            this.entityRowMapper);
    }

    @Override
    public Page<T> findAll(final Condition condition, final Pageable pageable) {
        final long count = this.count(condition);
        if (count == 0) {
            return new PageImpl<>(Collections.emptyList());
        }
        final Query query = StepUtils.pageableStep(this.persistentEntity,
            this.dsl.select(this.fieldsFromEntity)
                .from(this.table).where(condition), pageable);

        final List<T> list = this.jdbcOperations.query(query.getSQL(),
            query.getBindValues().toArray(),
            this.entityRowMapper);
        return new PageImpl<>(list, pageable, count);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public long count(final Condition condition) {

        final Query query = this.dsl.selectCount()
            .from(this.table)
            .where(condition);
        return this.jdbcOperations.queryForObject(query.getSQL(), query.getBindValues().toArray()
            , Long.class);
    }

    @Override
    public boolean exists(final Condition condition) {
        final Query query = this.dsl.selectOne().from(this.table).where(condition).limit(1);
        final Integer one = this.jdbcOperations.queryForObject(query.getSQL(),
            query.getBindValues().toArray(), Integer.class);
        return one != null;
    }

    @Override
    public List<T> findAll(final QueryStep queryStep) {
        final Query query = queryStep.apply(this.dsl);
        return this.jdbcOperations.query(query.getSQL(), query.getBindValues().toArray(),
            this.entityRowMapper);
    }

    @Override
    public List<T> findAll(final Query query) {
        return this.jdbcOperations.query(query.getSQL(), query.getBindValues().toArray(),
            this.entityRowMapper);
    }

    @Override
    public DSLContext dsl() {
        return this.dsl;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Optional<T> findByIdAndCreator(final ID id) {
        final Object auditor = this.auditorAware.getCurrentAuditor()
            .orElseThrow(() -> new IllegalStateException(AUDITOR_MUST_BE_NOT_NULL));

        final String createByColumn =
            Objects.requireNonNull(this.persistentEntity.getPersistentProperty(CreatedBy.class))
                .getColumnName().getReference();

        final Query query = this.dsl.select(this.fieldsFromEntity).from(this.table)
            .where(DSL.field(this.persistentEntity.getIdColumn().getReference()).eq(id))
            .and(DSL.field(createByColumn).eq(auditor)).limit(1);

        try {
            return Optional.of(this.jdbcOperations.queryForObject(query.getSQL(),
                query.getBindValues().toArray(),
                this.entityRowMapper));
        }
        catch (final EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }

    @Override
    public Iterable<T> findAllByCreator() {
        final Object auditor = this.auditorAware.getCurrentAuditor()
            .orElseThrow(() -> new IllegalStateException(AUDITOR_MUST_BE_NOT_NULL));

        final String createByColumn =
            Objects.requireNonNull(this.persistentEntity.getPersistentProperty(CreatedBy.class))
                .getColumnName().getReference();
        final Query query = this.dsl.select(this.fieldsFromEntity).from(this.table)
            .where(DSL.field(createByColumn).eq(auditor));

        return this.jdbcOperations.query(query.getSQL(), query.getBindValues().toArray(),
            this.entityRowMapper);
    }

    @Override
    public Page<T> findAllByCreator(final Pageable pageable) {
        final Object auditor = this.auditorAware.getCurrentAuditor()
            .orElseThrow(() -> new IllegalStateException(AUDITOR_MUST_BE_NOT_NULL));

        final long count = this.count();
        if (count == 0) {
            return new PageImpl<>(Collections.emptyList());
        }

        final String createByColumn =
            Objects.requireNonNull(this.persistentEntity.getPersistentProperty(CreatedBy.class))
                .getColumnName().getReference();
        final Query query =
            StepUtils.pageableStep(this.persistentEntity,
                this.dsl.select(this.fieldsFromEntity).from(this.table)
                    .where(DSL.field(createByColumn).eq(auditor)), pageable);

        final List<T> list = this.jdbcOperations.query(query.getSQL(),
            query.getBindValues().toArray(),
            this.entityRowMapper);

        return new PageImpl<>(list, pageable, count);
    }

    @Transactional
    @Override
    public T updateByIdAndCreator(final T instance) {
        final Object auditor = this.auditorAware.getCurrentAuditor()
            .orElseThrow(() -> new IllegalStateException(AUDITOR_MUST_BE_NOT_NULL));

        final Update<?> updateStep = StepUtils.updateByIdAndCreatorStep(this.dsl,
            this.table,
            this.persistentEntity, auditor, instance);
        this.jdbcOperations.update(updateStep.getSQL(),
            this.conversion(updateStep.getBindValues().toArray()));
        return instance;
    }

    @Transactional
    @Override
    public void deleteByIdAndCreator(final ID id) {
        final Object auditor = this.auditorAware.getCurrentAuditor()
            .orElseThrow(() -> new IllegalStateException(AUDITOR_MUST_BE_NOT_NULL));

        final String createByColumn =
            Objects.requireNonNull(this.persistentEntity.getPersistentProperty(CreatedBy.class))
                .getColumnName().getReference();

        final Query query = this.dsl.deleteFrom(this.table)
            .where(DSL.field(this.persistentEntity.getIdColumn().getReference()).eq(id))
            .and(DSL.field(createByColumn).eq(auditor))
            .limit(1);
        this.jdbcOperations.update(query.getSQL(), query.getBindValues().toArray());
    }

    Object[] conversion(final Object[] objs) {
        if (objs.length == 0) {
            return objs;
        }
        final Object[] params = new Object[objs.length];
        for (int i = 0, le = objs.length; i < le; i++) {
            if (objs[i] instanceof JSONB) {
                params[i] = this.jdbcConverter.writeJdbcValue(objs[i], JSONB.class, Types.OTHER);
            }
            else {
                params[i] = objs[i];
            }
        }

        return params;
    }
}
