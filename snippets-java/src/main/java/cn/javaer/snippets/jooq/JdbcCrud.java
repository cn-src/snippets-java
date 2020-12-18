package cn.javaer.snippets.jooq;

import java.util.Objects;

/**
 * @author cn-src
 */
public class JdbcCrud {
    private final CrudStep crudStep;

    public JdbcCrud(final CrudStep crudStep) {
        Objects.requireNonNull(crudStep);
        this.crudStep = crudStep;
    }

    <T> void insert(final T entity) {
        Objects.requireNonNull(entity);

        @SuppressWarnings("unchecked") final Class<T> clazz = (Class<T>) entity.getClass();
        final TableMetaProvider<T> tableMeta = CrudReflection.getTableMeta(clazz);
        this.crudStep.insertStep(entity, tableMeta).execute();
    }
}
