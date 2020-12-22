package cn.javaer.snippets.jooq;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author cn-src
 */
public class JdbcCrud {
    private final CrudStep crudStep;

    public JdbcCrud(final CrudStep crudStep) {
        Objects.requireNonNull(crudStep);
        this.crudStep = crudStep;
    }

    public JdbcCrud(final DSLContext dsl) {
        Objects.requireNonNull(dsl);
        this.crudStep = new CrudStep(dsl, Optional::empty);
    }

    public JdbcCrud(final DSLContext dsl, final AuditorAware<?> auditorAware) {
        Objects.requireNonNull(dsl);
        Objects.requireNonNull(auditorAware);
        this.crudStep = new CrudStep(dsl, auditorAware);
    }

    public <T, ID> Optional<T> findById(final ID id, final Class<T> clazz) {
        Objects.requireNonNull(clazz);

        return this.crudStep.findByIdStep(id, CrudReflection.getTableMeta(clazz))
            .fetchOptionalInto(clazz);
    }

    public <T, ID> Optional<T> findByIdAndCreator(final ID id, final Class<T> clazz) {
        Objects.requireNonNull(clazz);

        return this.crudStep.findByIdAndCreatorStep(id, CrudReflection.getTableMeta(clazz))
            .fetchOptionalInto(clazz);
    }

    public <T> List<T> findAll(final Class<T> clazz) {
        final TableMetaProvider<T, ?, ?> meta = CrudReflection.getTableMeta(clazz);
        return this.crudStep.findAllStep(meta).fetchInto(clazz);
    }

    public <T> List<T> findAllByCreator(final Class<T> clazz) {
        final TableMetaProvider<T, ?, ?> meta = CrudReflection.getTableMeta(clazz);
        return this.crudStep.findAllByCreatorStep(meta).fetchInto(clazz);
    }

    public <T> int insert(final T entity) {
        Objects.requireNonNull(entity);

        @SuppressWarnings("unchecked")
        final Class<T> clazz = (Class<T>) entity.getClass();
        final TableMetaProvider<T, ?, ?> tableMeta = CrudReflection.getTableMeta(clazz);
        return this.crudStep.insertStep(entity, tableMeta).execute();
    }

    public <T> int batchInsert(final List<T> entities) {
        Validate.notEmpty(entities);

        @SuppressWarnings("unchecked")
        final Class<T> clazz = (Class<T>) entities.get(0).getClass();
        final TableMetaProvider<T, ?, ?> tableMeta = CrudReflection.getTableMeta(clazz);
        return this.crudStep.batchInsertStep(entities, tableMeta).execute();
    }

    public <T> int update(final T entity) {
        Objects.requireNonNull(entity);

        @SuppressWarnings("unchecked")
        final Class<T> clazz = (Class<T>) entity.getClass();
        final TableMetaProvider<T, ?, ?> meta = CrudReflection.getTableMeta(clazz);
        return this.crudStep.dynamicUpdateStep(entity, meta, o -> true).execute();
    }

    public <T> int dynamicUpdate(final T entity) {
        Objects.requireNonNull(entity);

        @SuppressWarnings("unchecked")
        final Class<T> clazz = (Class<T>) entity.getClass();
        final TableMetaProvider<T, ?, ?> meta = CrudReflection.getTableMeta(clazz);
        return this.crudStep.dynamicUpdateStep(entity, meta, ObjectUtils::isNotEmpty).execute();
    }

    public <T> int dynamicUpdateByCreator(final T entity) {
        Objects.requireNonNull(entity);

        @SuppressWarnings("unchecked")
        final Class<T> clazz = (Class<T>) entity.getClass();
        final TableMetaProvider<T, ?, ?> meta = CrudReflection.getTableMeta(clazz);
        return this.crudStep.dynamicUpdateByCreatorStep(entity, meta, ObjectUtils::isNotEmpty)
            .execute();
    }
}
