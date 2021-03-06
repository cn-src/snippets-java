package cn.javaer.snippets.jooq;

import cn.javaer.snippets.util.ReflectionUtils;
import cn.javaer.snippets.util.StrUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.Nullable;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author cn-src
 */
public class CrudReflection {
    @SuppressWarnings("rawtypes")
    private static final ConcurrentMap<Class, TableMeta> META_CACHE =
        new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T, ID, A> TableMeta<T, ID, A> getTableMeta(final Class<T> entityClass) {
        return META_CACHE.computeIfAbsent(entityClass, it -> {
            try {
                return initTableMeta(it);
            }
            catch (final NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private static Table<?> getTable(final Class<?> clazz) {
        return ReflectionUtils
            .getAnnotationAttributeValue(
                clazz, "org.springframework.data.relational.core.mapping.Table", "value")
            .map(String.class::cast)
            .map(DSL::table)
            .orElseGet(() -> DSL.table(StrUtils.toSnakeLower(clazz.getSimpleName())));
    }

    @SuppressWarnings({"unchecked", "AlibabaMethodTooLong"})
    @Nullable
    private static <T, ID, A> TableMetaImpl<T, ID, A> initTableMeta(final Class<T> entityClass)
        throws NoSuchFieldException, IllegalAccessException {

        final Field[] fields = FieldUtils.getAllFields(entityClass);
        if (fields == null || fields.length == 0) {
            return null;
        }
        final TableMetaImpl.TableMetaImplBuilder<T, ID, A> builder = TableMetaImpl.builder();
        builder.table(getTable(entityClass))
            .entityClass(entityClass);

        final List<org.jooq.Field<Object>> selectColumns = new ArrayList<>();
        final List<ColumnMeta<T, ?>> saveColumns = new ArrayList<>();
        for (final Field field : fields) {
            if (Modifier.isTransient(field.getModifiers()) || ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.Transient")) {
                continue;
            }

            final Optional<MethodHandle> getterMethodOpt =
                ReflectionUtils.findGetterByField(entityClass, field);
            if (!getterMethodOpt.isPresent()) {
                continue;
            }

            final Optional<String> columnOpt = ReflectionUtils.getAnnotationAttributeValue(
                field, "org.springframework.data.relational.core.mapping.Column", "value")
                .map(String.class::cast);
            final Optional<String> columnSfmOpt = ReflectionUtils.getAnnotationAttributeValue(
                field, "org.simpleflatmapper.map.annotation.Column", "value")
                .map(String.class::cast);
            final String columnName =
                columnOpt.orElseGet(() -> columnSfmOpt.orElse(StrUtils.toSnakeLower
                    (field.getName())));

            final Class<?> fieldType = field.getType();
            final org.jooq.Field<Object> column =
                (org.jooq.Field<Object>) DSL.field(columnName, fieldType);
            final MethodHandle handle = getterMethodOpt.get();
            final ColumnMeta<T, ?> columnMeta = new ColumnMeta<>(o -> {
                try {
                    return handle.invoke(o);
                }
                catch (final Throwable t) {
                    throw new IllegalStateException(t);
                }
            }, column);
            if (!field.isAnnotationPresent(ExcludeSelect.class)) {
                selectColumns.add(column);
            }
            if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.Id")) {
                builder.id((ColumnMeta<T, ID>) columnMeta);
                builder.idReadOnly(ReflectionUtils.isAnnotated(field,
                    "org.springframework.data.annotation.ReadOnlyProperty"));
            }
            else if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.CreatedBy")) {
                builder.createdBy((ColumnMeta<T, A>) columnMeta);
            }
            else if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.CreatedDate")) {
                builder.createdDate((ColumnMeta<T, LocalDateTime>) columnMeta);
            }
            else if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.LastModifiedBy")) {
                builder.updatedBy((ColumnMeta<T, A>) columnMeta);
            }
            else if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.LastModifiedDate")) {
                builder.updatedDate((ColumnMeta<T, LocalDateTime>) columnMeta);
            }
            else if (!ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.ReadOnlyProperty") ||
                !field.isAnnotationPresent(ExcludeSaved.class)) {
                saveColumns.add(columnMeta);
            }
        }
        return builder.selectFields(Collections.unmodifiableList(selectColumns))
            .savedColumnMetas(Collections.unmodifiableList(saveColumns))
            .build();
    }
}
