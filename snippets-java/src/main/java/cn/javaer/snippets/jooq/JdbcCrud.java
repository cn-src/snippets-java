package cn.javaer.snippets.jooq;

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
        final TableMetaProvider<T> meta = CrudReflection.getTableMeta(clazz);
        return this.crudStep.findAllStep(meta).fetchInto(clazz);
    }

    public <T> List<T> findAllByCreator(final Class<T> clazz) {
        final TableMetaProvider<T> meta = CrudReflection.getTableMeta(clazz);
        return this.crudStep.findAllByCreatorStep(meta).fetchInto(clazz);
    }

    public <T> void insert(final T entity) {
        Objects.requireNonNull(entity);

        @SuppressWarnings("unchecked") final Class<T> clazz = (Class<T>) entity.getClass();
        final TableMetaProvider<T> tableMeta = CrudReflection.getTableMeta(clazz);
        this.crudStep.insertStep(entity, tableMeta).execute();
    }
}
