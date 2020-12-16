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

    public CrudStep(@NotNull final DSLContext dsl, AuditorAware<?> auditorAware) {
        Objects.requireNonNull(dsl);

        this.dsl = dsl;
        this.auditorAware = auditorAware;
    }

    public <T, ID> SelectConditionStep<Record> findByIdAndCreatorStep(
        final @NotNull ID id, final @NotNull Class<T> entityClass) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(entityClass);

        @SuppressWarnings("unchecked") final Field<ID> idColumn =
            (Field<ID>) Objects.requireNonNull(CrudReflection.getIdColumnMeta(entityClass))
                .getColumn();

        Field<Object> creatorColumn =
            Objects.requireNonNull(CrudReflection.getCreatorColumnMeta(entityClass))
                .getColumn();

        return this.dsl.select(CrudReflection.getFields(entityClass))
            .from(CrudReflection.getTable(entityClass))
            .where(idColumn.eq(id))
            .and(creatorColumn.eq(auditorAware.requiredAuditor()));
    }

    /**
     * jOOQ 实体插入.
     *
     * @param entity the entity
     *
     * @return the insert values step n
     */
    public InsertValuesStepN<?> insertStep(final @NotNull Object entity) {
        Objects.requireNonNull(entity);

        final List<ColumnMeta> columnMetas = CrudReflection.getColumnMetas(entity.getClass());
        final List<Object> values = new ArrayList<>();
        final List<Field<?>> fields = new ArrayList<>();
        final MethodAccess methodAccess = MethodAccess.get(entity.getClass());
        Object auditor = auditorAware.getCurrentAuditor().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        for (final ColumnMeta cm : columnMetas) {
            fields.add(cm.getColumn());
            insertValue(methodAccess, entity, values, cm, auditor, now);
        }
        return this.dsl.insertInto(CrudReflection.getTable(entity.getClass()))
            .columns(fields).values(values);
    }

    /**
     * jOOQ 实体批量插入.
     *
     * @param entities the entities
     *
     * @return the insert values step n
     */
    public InsertValuesStepN<?> batchInsertStep(@NotNull final List<?> entities) {
        Validate.notEmpty(entities);

        final Class<?> clazz = entities.get(0).getClass();
        final List<ColumnMeta> columnMetas = CrudReflection.getColumnMetas(clazz);
        final List<Field<?>> fields = new ArrayList<>();
        for (final ColumnMeta cm : columnMetas) {
            fields.add(cm.getColumn());
        }
        final InsertValuesStepN<?> step =
            this.dsl.insertInto(CrudReflection.getTable(clazz)).columns(fields);
        final MethodAccess methodAccess = MethodAccess.get(clazz);
        Object auditor = auditorAware.getCurrentAuditor().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        for (final Object entity : entities) {
            final List<Object> rowValue = new ArrayList<>();
            for (final ColumnMeta cm : columnMetas) {
                insertValue(methodAccess, entity, rowValue, cm, auditor, now);
            }
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
    public UpdateConditionStep<?> dynamicUpdateStep(@NotNull final Object entity) {
        Objects.requireNonNull(entity);

        final Class<?> clazz = entity.getClass();
        final Map<Field<?>, Object> dynamic = new HashMap<>();
        final List<ColumnMeta> columnMetas = CrudReflection.getColumnMetas(clazz);
        final MethodAccess methodAccess = MethodAccess.get(clazz);
        Field<Object> idColumn = null;
        Object idValue = null;
        for (final ColumnMeta cm : columnMetas) {
            final Object value = methodAccess.invoke(entity, cm.getGetterName());
            if (cm.isId()) {
                idColumn = cm.getColumn();
                idValue = value;
                continue;
            }
            if (cm.isCreator() || cm.isCreatedDate()) {
                continue;
            }
            if (cm.isUpdater()) {
                dynamic.put(cm.getColumn(), auditorAware.requiredAuditor());
                continue;
            }
            if (cm.isUpdateDate()) {
                dynamic.put(cm.getColumn(), LocalDateTime.now());
                continue;
            }
            if (ObjectUtils.isNotEmpty(value)) {
                dynamic.put(cm.getColumn(), value);
            }
        }

        return this.dsl.update(CrudReflection.getTable(clazz))
            .set(dynamic).where(Objects.requireNonNull(idColumn).eq(idValue));
    }

    public UpdateConditionStep<?> dynamicUpdateByCreatorStep(@NotNull final Object entity) {
        Objects.requireNonNull(entity);

        final Class<?> clazz = entity.getClass();
        final Map<Field<?>, Object> dynamic = new HashMap<>();
        final List<ColumnMeta> columnMetas = CrudReflection.getColumnMetas(clazz);
        final MethodAccess methodAccess = MethodAccess.get(clazz);
        Field<Object> idColumn = null;
        Object idValue = null;
        Field<Object> creatorColumn = null;
        Object auditor = auditorAware.requiredAuditor();
        for (final ColumnMeta cm : columnMetas) {
            final Object value = methodAccess.invoke(entity, cm.getGetterName());
            if (cm.isId()) {
                idColumn = cm.getColumn();
                idValue = value;
                continue;
            }
            if (cm.isCreatedDate()) {
                continue;
            }
            if (cm.isCreator()) {
                creatorColumn = cm.getColumn();
                continue;
            }

            if (cm.isUpdater()) {
                dynamic.put(cm.getColumn(), auditor);
                continue;
            }
            if (cm.isUpdateDate()) {
                dynamic.put(cm.getColumn(), LocalDateTime.now());
                continue;
            }
            if (ObjectUtils.isNotEmpty(value)) {
                dynamic.put(cm.getColumn(), value);
            }
        }

        return this.dsl.update(CrudReflection.getTable(clazz))
            .set(dynamic).where(Objects.requireNonNull(idColumn).eq(idValue))
            .and(Objects.requireNonNull(creatorColumn).eq(auditor));
    }

    private void insertValue(MethodAccess methodAccess, Object entity,
                             List<Object> rowValue, ColumnMeta cm,
                             Object auditor, LocalDateTime updateDate) {
        if (cm.isCreator() || cm.isUpdater()) {
            rowValue.add(Objects.requireNonNull(auditor));
            return;
        }
        if (cm.isCreatedDate() || cm.isUpdateDate()) {
            rowValue.add(updateDate);
            return;
        }
        rowValue.add(methodAccess.invoke(entity, cm.getGetterName()));
    }
}
