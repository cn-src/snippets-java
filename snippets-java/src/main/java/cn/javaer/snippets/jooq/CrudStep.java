package cn.javaer.snippets.jooq;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.UpdateConditionStep;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author cn-src
 */
public class CrudStep {
    private final DSLContext dsl;
    private final AuditorAware<?> auditorAware;

    public CrudStep(@NotNull final DSLContext dsl, final AuditorAware<?> auditorAware) {
        this.dsl = dsl;
        this.auditorAware = auditorAware;
    }

    /**
     * 根据 id 查询.
     *
     * @param <M> 元数据泛型
     * @param <ID> id 泛型
     * @param id the id
     * @param meta the meta
     *
     * @return the select step
     */
    public <M extends TableMetaProvider<?, ID, ?>, ID> SelectConditionStep<Record>
    findByIdStep(final @NotNull ID id, final M meta) {

        return this.dsl.select(meta.selectFields())
            .from(meta.getTable())
            .where(meta.getId().getColumn().eq(id));
    }

    /**
     * 根据 id 和创建者查询.
     *
     * @param <M> 元数据泛型
     * @param <ID> id 泛型
     * @param id the id
     * @param meta the meta
     *
     * @return the select step
     */
    public <M extends TableMetaProvider<?, ID, A>, ID, A> SelectConditionStep<Record>
    findByIdAndCreatorStep(final @NotNull ID id, final M meta) {

        @SuppressWarnings("unchecked")
        final A auditor = (A) this.auditorAware.requiredAuditor();
        return this.dsl.select(meta.selectFields())
            .from(meta.getTable())
            .where(meta.getId().getColumn().eq(id))
            .and(meta.getCreatedBy().getColumn().eq(auditor));
    }

    public @NotNull <M extends TableMetaProvider<?, ?, ?>> SelectJoinStep<Record>
    findAllStep(final M meta) {

        return this.dsl.select(meta.selectFields())
            .from(meta.getTable());
    }

    public @NotNull <M extends TableMetaProvider<?, ?, A>, A> SelectConditionStep<Record>
    findAllByCreatorStep(final M meta) {

        @SuppressWarnings("unchecked")
        final A auditor = (A) this.auditorAware.requiredAuditor();
        return this.dsl.select(meta.selectFields())
            .from(meta.getTable())
            .where(meta.getCreatedBy().getColumn().eq(auditor));
    }

    /**
     * 插入实体.
     *
     * @param <T> the type parameter
     * @param <M> the type parameter
     * @param entity the entity
     * @param meta the meta
     *
     * @return the insert values step n
     */
    public <M extends TableMetaProvider<T, ID, A>, T, ID, A> InsertValuesStepN<?>
    insertStep(final @NotNull T entity, final M meta) {

        final List<Object> values = new ArrayList<>();
        final List<Field<?>> fields = new ArrayList<>();
        final Object auditor = this.auditorAware.getCurrentAuditor().orElse(null);
        final LocalDateTime now = LocalDateTime.now();
        meta.idGenerator().ifPresent(it -> {
            fields.add(it.getColumn());
            values.add(it.getReadMethod().apply(entity));
        });

        for (final ColumnMeta<T, ?> cm : meta.saveColumnMetas()) {
            fields.add(cm.getColumn());
            values.add(cm.getReadMethod().apply(entity));
        }
        meta.updatedBy().ifPresent(it -> {
            fields.add(it.getColumn());
            values.add(auditor);
        });
        meta.updatedDate().ifPresent(it -> {
            fields.add(it.getColumn());
            values.add(now);
        });
        meta.createdBy().ifPresent(it -> {
            fields.add(it.getColumn());
            values.add(auditor);
        });
        meta.createdDate().ifPresent(it -> {
            fields.add(it.getColumn());
            values.add(now);
        });
        return this.dsl.insertInto(meta.getTable())
            .columns(fields).values(values);
    }

    /**
     * 批量插入实体.
     *
     * @param <T> the type parameter
     * @param <M> the type parameter
     * @param entities the entities
     * @param meta the meta
     *
     * @return the insert values step n
     */
    public <M extends TableMetaProvider<T, ID, A>, T, ID, A> InsertValuesStepN<?>
    batchInsertStep(@NotNull final List<T> entities, final M meta) {

        final List<Field<?>> fields = new ArrayList<>(meta.saveColumnMetas().size() + 4);
        meta.idGenerator().ifPresent(it -> fields.add(it.getColumn()));
        meta.saveColumnMetas().forEach(it -> fields.add(it.getColumn()));
        meta.updatedBy().ifPresent(it -> fields.add(it.getColumn()));
        meta.updatedDate().ifPresent(it -> fields.add(it.getColumn()));
        meta.createdBy().ifPresent(it -> fields.add(it.getColumn()));
        meta.createdDate().ifPresent(it -> fields.add(it.getColumn()));

        final Object auditor = this.auditorAware.getCurrentAuditor().orElse(null);
        final LocalDateTime now = LocalDateTime.now();

        final InsertValuesStepN<?> step = this.dsl.insertInto(meta.getTable()).columns(fields);

        for (final T entity : entities) {
            final List<Object> rowValue = new ArrayList<>();
            meta.idGenerator().ifPresent(it ->
                rowValue.add(it.getReadMethod().apply(entity)));
            meta.saveColumnMetas().forEach(it ->
                rowValue.add(it.getReadMethod().apply(entity)));
            meta.updatedBy().ifPresent(it -> rowValue.add(auditor));
            meta.updatedDate().ifPresent(it -> rowValue.add(now));
            meta.createdBy().ifPresent(it -> rowValue.add(auditor));
            meta.createdDate().ifPresent(it -> rowValue.add(now));
            step.values(rowValue.toArray());
        }
        return step;
    }

    /**
     * 根据 id 动态更新实体.
     *
     * @param <T> the type parameter
     * @param <M> the type parameter
     * @param entity the entity
     * @param meta the meta
     * @param include 是否持久化此值
     *
     * @return the update set more step
     */
    public <M extends TableMetaProvider<T, ID, A>, T, ID, A> UpdateConditionStep<?>
    dynamicUpdateStep(@NotNull final T entity, final M meta, final Predicate<Object> include) {

        final Map<Field<?>, Object> dynamic = new HashMap<>(10);
        for (final ColumnMeta<T, ?> cm : meta.saveColumnMetas()) {
            final Object value = cm.getReadMethod().apply(entity);
            if (include.test(value)) {
                dynamic.put(cm.getColumn(), value);
            }
        }
        meta.updatedBy().ifPresent(it ->
            dynamic.put(it.getColumn(), this.auditorAware.getCurrentAuditor().orElse(null)));
        meta.updatedDate().ifPresent(it -> dynamic.put(it.getColumn(), LocalDateTime.now()));

        final ID idValue = meta.getId().getReadMethod().apply(entity);
        return this.dsl.update(meta.getTable())
            .set(dynamic).where(meta.getId().getColumn().eq(idValue));
    }

    /**
     * 根据 id 和创建者动态更新实体.
     *
     * @param <T> the type parameter
     * @param <M> the type parameter
     * @param entity the entity
     * @param meta the meta
     * @param include 是否持久化此值
     *
     * @return the update set more step
     */
    public <M extends TableMetaProvider<T, ID, A>, T, ID, A> UpdateConditionStep<?>
    dynamicUpdateByCreatorStep(@NotNull final T entity, final M meta,
                               final Predicate<Object> include) {
        final UpdateConditionStep<?> step = this.dynamicUpdateStep(entity, meta, include);
        @SuppressWarnings("unchecked")
        final A auditor = (A) this.auditorAware.requiredAuditor();
        return step.and(meta.getCreatedBy().getColumn().eq(auditor));
    }
}
