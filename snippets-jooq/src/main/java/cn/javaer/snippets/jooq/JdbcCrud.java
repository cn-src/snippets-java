package cn.javaer.snippets.jooq;

import cn.javaer.snippets.model.Page;
import cn.javaer.snippets.model.PageParam;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.JDBCUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
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

    public JdbcCrud(final DataSource dataSource) {
        Objects.requireNonNull(dataSource);
        try (final Connection conn = dataSource.getConnection()) {
            final DSLContext dsl = DSL.using(dataSource, JDBCUtils.dialect(conn));
            this.crudStep = new CrudStep(dsl, Optional::empty);
        }
        catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
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

    public DSLContext dsl() {
        return this.crudStep.dsl();
    }

    public <T, ID, M extends TableMetaProvider<T, ID, ?>>
    Optional<T> findById(final M meta, final ID id) {
        Objects.requireNonNull(meta);
        return this.crudStep.findByIdStep(meta, id).fetchOptionalInto(meta.getEntityClass());
    }

    public <T, ID, A, M extends TableMetaProvider<T, ID, A>>
    Optional<T> findByIdAndCreator(final M meta, final ID id) {
        Objects.requireNonNull(meta);
        return this.crudStep.findByIdAndCreatorStep(meta, id)
            .fetchOptionalInto(meta.getEntityClass());
    }

    public <T, ID, M extends TableMetaProvider<T, ID, ?>>
    Optional<T> findOne(final M meta, final Condition condition) {
        Objects.requireNonNull(meta);
        return this.crudStep.findOneStep(meta, condition)
            .fetchOptionalInto(meta.getEntityClass());
    }

    public <T, M extends TableMetaProvider<T, ?, ?>>
    List<T> findAll(final M meta) {
        Objects.requireNonNull(meta);
        return this.crudStep.findAllStep(meta).fetchInto(meta.getEntityClass());
    }

    public <T, M extends TableMetaProvider<T, ?, ?>>
    Page<T> findAll(final M meta, final PageParam pageParam) {
        Objects.requireNonNull(meta);
        final List<T> content = this.crudStep.findAllStep(meta, pageParam)
            .fetchInto(meta.getEntityClass());
        return Page.of(content, this.crudStep.dsl().fetchCount(meta.getTable()));
    }

    public <T, M extends TableMetaProvider<T, ?, ?>>
    Page<T> findAll(final M meta, final Condition condition, final PageParam pageParam) {
        Objects.requireNonNull(meta);
        final List<T> content = this.crudStep.findAllStep(meta, condition, pageParam)
            .fetchInto(meta.getEntityClass());
        final int total = this.crudStep.dsl().fetchCount(meta.getTable(), condition);
        return Page.of(content, total);
    }

    public <T, M extends TableMetaProvider<T, ?, ?>>
    List<T> findAll(final M meta, final Condition condition) {
        Objects.requireNonNull(meta);
        return this.crudStep.findAllStep(meta).where(condition).fetchInto(meta.getEntityClass());
    }

    public <T, A, M extends TableMetaProvider<T, ?, A>>
    List<T> findAllByCreator(final M meta) {
        Objects.requireNonNull(meta);
        return this.crudStep.findAllByCreatorStep(meta).fetchInto(meta.getEntityClass());
    }

    public <T, ID, A, M extends TableMetaProvider<T, ID, A>>
    Page<T> findAllByCreator(final M meta, final PageParam pageParam) {
        Objects.requireNonNull(meta);

        final List<T> content = this.crudStep.findAllByCreatorStep(meta)
            .offset(pageParam.getOffset()).limit(pageParam.getSize())
            .fetchInto(meta.getEntityClass());
        final int total = this.crudStep.dsl().fetchCount(meta.getTable(),
            this.crudStep.creatorCondition(meta));

        return Page.of(content, total);
    }

    public <T, ID, A, M extends TableMetaProvider<T, ID, A>>
    Page<T> findAllByCreator(final M meta, final Condition condition, final PageParam pageParam) {
        Objects.requireNonNull(meta);

        final List<T> content = this.crudStep.findAllByCreatorStep(meta)
            .and(condition)
            .offset(pageParam.getOffset()).limit(pageParam.getSize())
            .fetchInto(meta.getEntityClass());
        final int total = this.crudStep.dsl().fetchCount(meta.getTable(),
            this.crudStep.creatorCondition(meta).and(condition));

        return Page.of(content, total);
    }

    public <T, ID, A, M extends TableMetaProvider<T, ID, A>>
    ID insert(final M meta, final T entity) {
        Objects.requireNonNull(meta);
        Objects.requireNonNull(entity);
        return Objects.requireNonNull(this.crudStep.insertStep(meta, entity)
            .returningResult(meta.id().getColumn())
            .fetchOne()).value1();
    }

    public <T, ID, A, M extends TableMetaProvider<T, ID, A>>
    int batchInsert(final M meta, final List<T> entities) {
        Objects.requireNonNull(meta);
        Validate.notEmpty(entities);
        return this.crudStep.batchInsertStep(entities, meta).execute();
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T, ID, A, M extends TableMetaProvider<T, ID, A>>
    int update(final M meta, final T entity) {
        Objects.requireNonNull(meta);
        Objects.requireNonNull(entity);

        return this.crudStep.dynamicUpdateStep(entity, meta, o -> true).execute();
    }

    public <T, ID, A, M extends TableMetaProvider<T, ID, A>>
    int dynamicUpdate(final M meta, final T entity) {
        Objects.requireNonNull(meta);
        Objects.requireNonNull(entity);
        return this.crudStep.dynamicUpdateStep(entity, meta, ObjectUtils::isNotEmpty).execute();
    }

    public <T, ID, A, M extends TableMetaProvider<T, ID, A>>
    int dynamicUpdateByCreator(final M meta, final T entity) {
        Objects.requireNonNull(meta);
        Objects.requireNonNull(entity);

        return this.crudStep.dynamicUpdateByCreatorStep(entity, meta, ObjectUtils::isNotEmpty)
            .execute();
    }

    public <T, ID, A, M extends TableMetaProvider<T, ID, A>>
    int deleteById(final M meta, final ID id) {
        Objects.requireNonNull(meta);
        return this.crudStep.dsl().deleteFrom(meta.getTable())
            .where(meta.id().getColumn().eq(id))
            .execute();
    }
}
