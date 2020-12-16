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
import org.jooq.UpdateSetMoreStep;

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

    public CrudStep(@NotNull final DSLContext dsl) {
        this.dsl = dsl;
    }

    <T, ID> SelectConditionStep<Record> findByIdAndCreatorStep(
        final @NotNull ID id, final @NotNull Class<T> entityClass) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(entityClass);

        @SuppressWarnings("unchecked") final Field<ID> idColumn =
            (Field<ID>) Objects.requireNonNull(CrudReflection.getIdColumnMeta(entityClass))
                .getColumn();
        return this.dsl.select(CrudReflection.getFields(entityClass))
            .from(CrudReflection.getTable(entityClass))
            .where(idColumn.eq(id));
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
        for (final ColumnMeta cm : columnMetas) {
            fields.add(cm.getColumn());
            values.add(methodAccess.invoke(entity, cm.getGetterName()));
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
        for (final Object entity : entities) {
            final List<Object> rowValue = new ArrayList<>();
            for (final ColumnMeta cm : columnMetas) {
                rowValue.add(methodAccess.invoke(entity, cm.getGetterName()));
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
    public UpdateSetMoreStep<?> dynamicUpdateStep(@NotNull final Object entity) {
        Objects.requireNonNull(entity);

        final Class<?> clazz = entity.getClass();
        final Map<Field<?>, Object> dynamic = new HashMap<>();
        final List<ColumnMeta> columnMetas = CrudReflection.getColumnMetas(clazz);
        final MethodAccess methodAccess = MethodAccess.get(clazz);
        for (final ColumnMeta cm : columnMetas) {
            final Object value = methodAccess.invoke(entity, cm.getGetterName());
            if (ObjectUtils.isNotEmpty(value)) {
                dynamic.put(cm.getColumn(), value);
            }
        }
        return this.dsl.update(CrudReflection.getTable(clazz)).set(dynamic);
    }
}
