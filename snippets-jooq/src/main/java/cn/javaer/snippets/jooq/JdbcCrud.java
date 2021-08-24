package cn.javaer.snippets.jooq;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.javaer.snippets.model.Page;
import cn.javaer.snippets.model.PageParam;
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

    public <T, ID> Optional<T> findById(final TableMeta<T, ID, ?> meta, final ID id) {
        Objects.requireNonNull(meta);
        return this.crudStep.findByIdStep(meta, id).fetchOptionalInto(meta.getEntityClass());
    }

    public <T, ID> Optional<T>
    findByIdAndCreator(final TableMeta<T, ID, ?> meta, final ID id) {
        Objects.requireNonNull(meta);
        return this.crudStep.findByIdAndCreatorStep(meta, id)
            .fetchOptionalInto(meta.getEntityClass());
    }

    public <T> Optional<T>
    findOne(final TableMeta<T, ?, ?> meta, final Condition condition) {
        Objects.requireNonNull(meta);
        return this.crudStep.findOneStep(meta, condition)
            .fetchOptionalInto(meta.getEntityClass());
    }

    public <T> List<T>
    findAll(final TableMeta<T, ?, ?> meta) {
        Objects.requireNonNull(meta);
        return this.crudStep.findAllStep(meta).fetchInto(meta.getEntityClass());
    }

    public <T> Page<T>
    findAll(final TableMeta<T, ?, ?> meta, final PageParam pageParam) {
        Objects.requireNonNull(meta);
        final List<T> content = this.crudStep.findAllStep(meta, pageParam)
            .fetchInto(meta.getEntityClass());
        return Page.of(content, this.crudStep.dsl().fetchCount(meta.getTable()));
    }

    public <T> Page<T>
    findAll(final TableMeta<T, ?, ?> meta,
            final Condition condition, final PageParam pageParam) {
        Objects.requireNonNull(meta);
        final List<T> content = this.crudStep.findAllStep(meta, condition, pageParam)
            .fetchInto(meta.getEntityClass());
        final int total = this.crudStep.dsl().fetchCount(meta.getTable(), condition);
        return Page.of(content, total);
    }

    public <T> List<T>
    findAll(final TableMeta<T, ?, ?> meta, final Condition condition) {
        Objects.requireNonNull(meta);
        return this.crudStep.findAllStep(meta, condition).fetchInto(meta.getEntityClass());
    }

    public <T> List<T>
    findAllByCreator(final TableMeta<T, ?, ?> meta) {
        Objects.requireNonNull(meta);
        return this.crudStep.findAllByCreatorStep(meta).fetchInto(meta.getEntityClass());
    }

    public <T> Page<T>
    findAllByCreator(final TableMeta<T, ?, ?> meta, final PageParam pageParam) {
        Objects.requireNonNull(meta);

        final List<T> content = this.crudStep.findAllByCreatorStep(meta)
            .offset(pageParam.getOffset()).limit(pageParam.getSize())
            .fetchInto(meta.getEntityClass());
        final int total = this.crudStep.dsl().fetchCount(meta.getTable(),
            this.crudStep.creatorCondition(meta));

        return Page.of(content, total);
    }

    public <T> Page<T>
    findAllByCreator(final TableMeta<T, ?, ?> meta,
                     final Condition condition, final PageParam pageParam) {
        Objects.requireNonNull(meta);

        final List<T> content = this.crudStep.findAllByCreatorStep(meta, condition)
            .offset(pageParam.getOffset()).limit(pageParam.getSize())
            .fetchInto(meta.getEntityClass());
        final int total = this.crudStep.dsl().fetchCount(meta.getTable(),
            this.crudStep.creatorCondition(meta).and(condition));

        return Page.of(content, total);
    }

    public <T, ID>
    ID insert(final TableMeta<T, ID, ?> meta, final T entity) {
        Objects.requireNonNull(meta);
        Objects.requireNonNull(entity);
        return Objects.requireNonNull(this.crudStep.insertStep(meta, entity)
            .returningResult(meta.id().getColumn())
            .fetchOne()).value1();
    }

    public <T> int
    batchInsert(final TableMeta<T, ?, ?> meta, final List<T> entities) {
        Objects.requireNonNull(meta);
        Assert.notEmpty(entities);
        return this.crudStep.batchInsertStep(meta, entities).execute();
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> int
    update(final TableMeta<T, ?, ?> meta, final T entity) {
        Objects.requireNonNull(meta);
        Objects.requireNonNull(entity);
        return this.crudStep.dynamicUpdateStep(meta, entity, o -> true).execute();
    }

    public <T> int
    dynamicUpdate(final TableMeta<T, ?, ?> meta, final T entity) {
        Objects.requireNonNull(meta);
        Objects.requireNonNull(entity);
        return this.crudStep.dynamicUpdateStep(meta, entity, ObjectUtil::isNotEmpty).execute();
    }

    public <T> int
    dynamicUpdateByCreator(final TableMeta<T, ?, ?> meta, final T entity) {
        Objects.requireNonNull(meta);
        Objects.requireNonNull(entity);

        return this.crudStep.dynamicUpdateByCreatorStep(meta, entity, ObjectUtil::isNotEmpty)
            .execute();
    }

    public <T, ID> int
    deleteById(final TableMeta<T, ID, ?> meta, final ID id) {
        Objects.requireNonNull(meta);
        return this.crudStep.dsl().deleteFrom(meta.getTable())
            .where(meta.id().getColumn().eq(id))
            .execute();
    }
}