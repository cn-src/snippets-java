package cn.javaer.snippets.jooq;

import cn.javaer.snippets.util.ReflectionUtils;
import cn.javaer.snippets.util.StrUtils;
import org.jetbrains.annotations.Nullable;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
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
    private static final ConcurrentMap<Class, TableMetaProvider> META_CACHE =
        new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> TableMetaProvider<T> getTableMeta(final Class<T> entityClass) {
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

    @Nullable
    private static <T> TableMeta<T> initTableMeta(final Class<T> entityClass)
        throws NoSuchFieldException, IllegalAccessException {

        final Field[] fields = entityClass.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return null;
        }
        final TableMeta.TableMetaBuilder<T> builder = TableMeta.builder();
        builder.table(getTable(entityClass))
            .entityClass(entityClass);

        final List<org.jooq.Field<Object>> selectColumns = new ArrayList<>();
        final List<ColumnMeta> saveColumns = new ArrayList<>();
        for (final Field field : fields) {
            if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.Transient")) {
                continue;
            }

            final String getterName = boolean.class.equals(field.getType()) ?
                "is" + StrUtils.toFirstCharUpper(field.getName())
                : "get" + StrUtils.toFirstCharUpper(field.getName());
            if (!ReflectionUtils.hasMethod(entityClass, getterName)) {
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
            @SuppressWarnings("unchecked")
            final org.jooq.Field<Object> column =
                (org.jooq.Field<Object>) DSL.field(columnName, fieldType);
            selectColumns.add(column);
            final MethodHandle handle = MethodHandles.lookup().findGetter(entityClass,
                field.getName(), fieldType);
            final ColumnMeta columnMeta = new ColumnMeta(field.getName(), o -> {
                try {
                    return handle.invoke(o);
                }
                catch (final Throwable t) {
                    throw new IllegalStateException(t);
                }
            }, column);
            if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.Id")) {
                builder.id(columnMeta);
                builder.idReadOnly(ReflectionUtils.isAnnotated(field,
                    "org.springframework.data.annotation.ReadOnlyProperty"));
            }
            else if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.CreatedBy")) {
                builder.createdBy(columnMeta);
            }
            else if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.CreatedDate")) {
                builder.createdDate(columnMeta);
            }
            else if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.LastModifiedBy")) {
                builder.updatedBy(columnMeta);
            }
            else if (ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.LastModifiedDate")) {
                builder.updatedDate(columnMeta);
            }
            else if (!ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.ReadOnlyProperty")) {
                saveColumns.add(columnMeta);
            }
        }
        return builder.selectFields(Collections.unmodifiableList(selectColumns))
            .saveColumnMetas(Collections.unmodifiableList(saveColumns))
            .build();
    }
}
