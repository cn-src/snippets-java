package cn.javaer.snippets.jooq;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.UpdateConditionStep;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author cn-src
 */
public class CrudStep {
    private final DSLContext dsl;
    private final AuditorAware<?> auditorAware;

    public CrudStep(@NotNull final DSLContext dsl, final AuditorAware<?> auditorAware) {
        Objects.requireNonNull(dsl);
        Objects.requireNonNull(auditorAware);

        this.dsl = dsl;
        this.auditorAware = auditorAware;
    }

    public <M extends TableMetaProvider<?>, ID> SelectConditionStep<Record>
    findByIdStep(final @NotNull ID id, final M meta) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(meta);

        return this.dsl.select(meta.selectFields())
            .from(meta.getTable())
            .where(meta.getId().getColumn().eq(id));
    }

    public <M extends TableMetaProvider<?>, ID> SelectConditionStep<Record>
    findByIdAndCreatorStep(final @NotNull ID id, final M meta) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(meta);

        return this.dsl.select(meta.selectFields())
            .from(meta.getTable())
            .where(meta.getId().getColumn().eq(id))
            .and(meta.getCreatedBy().getColumn().eq(this.auditorAware.requiredAuditor()));
    }

    /**
     * jOOQ 实体插入.
     *
     * @param entity the entity
     *
     * @return the insert values step n
     */
    public <T, M extends TableMetaProvider<T>> InsertValuesStepN<?>
    insertStep(final @NotNull T entity, final M meta) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(meta);

        final List<Object> values = new ArrayList<>();
        final List<Field<?>> fields = new ArrayList<>();
        final MethodAccess methodAccess = MethodAccess.get(entity.getClass());
        final Object auditor = this.auditorAware.getCurrentAuditor().orElse(null);
        final LocalDateTime now = LocalDateTime.now();
        for (final ColumnMeta cm : meta.saveColumnMetas()) {
            fields.add(cm.getColumn());
            values.add(methodAccess.invoke(entity, cm.getGetterName()));
        }
        meta.idGenerator().ifPresent(it -> {
            fields.add(it.getColumn());
            values.add(methodAccess.invoke(entity, it.getGetterName()));
        });
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
     * jOOQ 实体批量插入.
     *
     * @param entities the entities
     *
     * @return the insert values step n
     */
    public <T, M extends TableMetaProvider<T>> InsertValuesStepN<?>
    batchInsertStep(@NotNull final List<?> entities, final M meta) {
        Validate.notEmpty(entities);
        Objects.requireNonNull(meta);

        final List<Field<?>> fields = new ArrayList<>(meta.saveColumnMetas().size() + 4);
        meta.idGenerator().ifPresent(it -> fields.add(it.getColumn()));
        meta.saveColumnMetas().forEach(it -> fields.add(it.getColumn()));
        meta.updatedBy().ifPresent(it -> fields.add(it.getColumn()));
        meta.updatedDate().ifPresent(it -> fields.add(it.getColumn()));
        meta.createdBy().ifPresent(it -> fields.add(it.getColumn()));
        meta.createdDate().ifPresent(it -> fields.add(it.getColumn()));

        final MethodAccess methodAccess = MethodAccess.get(meta.getEntityClass());
        final Object auditor = this.auditorAware.getCurrentAuditor().orElse(null);
        final LocalDateTime now = LocalDateTime.now();

        final InsertValuesStepN<?> step = this.dsl.insertInto(meta.getTable()).columns(fields);

        for (final Object entity : entities) {
            final List<Object> rowValue = new ArrayList<>();
            meta.idGenerator().ifPresent(it -> methodAccess.invoke(entity, it.getGetterName()));
            meta.saveColumnMetas().forEach(it ->
                rowValue.add(methodAccess.invoke(entity, it.getGetterName())));
            meta.updatedBy().ifPresent(it -> rowValue.add(auditor));
            meta.updatedDate().ifPresent(it -> rowValue.add(now));
            meta.createdBy().ifPresent(it -> rowValue.add(auditor));
            meta.createdDate().ifPresent(it -> rowValue.add(now));
            step.values(rowValue.toArray());
        }
        return step;
    }

    /**
     * jOOQ 实体动态更新.
     *
     * @param entity the entity
     *
     * @return the update set more step
     */
    public <T, M extends TableMetaProvider<T>> UpdateConditionStep<?>
    dynamicUpdateStep(@NotNull final T entity, final M meta) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(meta);

        final Class<?> clazz = entity.getClass();
        final MethodAccess methodAccess = MethodAccess.get(clazz);
        final Map<Field<?>, Object> dynamic = new HashMap<>(10);
        for (final ColumnMeta cm : meta.saveColumnMetas()) {
            final Object value = methodAccess.invoke(entity, cm.getGetterName());
            if (ObjectUtils.isNotEmpty(value)) {
                dynamic.put(cm.getColumn(), value);
            }
        }
        meta.updatedBy().ifPresent(it ->
            dynamic.put(it.getColumn(), this.auditorAware.getCurrentAuditor().orElse(null)));
        meta.updatedDate().ifPresent(it -> dynamic.put(it.getColumn(), LocalDateTime.now()));

        final Object idValue = methodAccess.invoke(entity, meta.getId().getGetterName());
        return this.dsl.update(meta.getTable())
            .set(dynamic).where(meta.getId().getColumn().eq(idValue));
    }

    public <T, M extends TableMetaProvider<T>> UpdateConditionStep<?>
    dynamicUpdateByCreatorStep(@NotNull final T entity, final M meta) {
        final UpdateConditionStep<?> step = this.dynamicUpdateStep(entity, meta);
        final Object createdByValue = MethodAccess.get(entity.getClass())
            .invoke(entity, meta.getCreatedBy().getGetterName());
        return step.and(meta.getCreatedBy().getColumn().eq(createdByValue));
    }
}
