package cn.javaer.snippets.jooq;

import cn.javaer.snippets.util.ReflectionUtils;
import cn.javaer.snippets.util.StrUtils;
import org.jetbrains.annotations.UnmodifiableView;
import org.jooq.Table;
import org.jooq.impl.DSL;

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
    private static final ConcurrentMap<Class<?>, List<ColumnMeta>> COLUMN_META_CACHE =
        new ConcurrentHashMap<>();

    private static final ConcurrentMap<Class<?>, Table<?>> TABLE_CACHE =
        new ConcurrentHashMap<>();

    public static Table<?> getTable(final Class<?> clazz) {
        return TABLE_CACHE.computeIfAbsent(clazz, it -> ReflectionUtils.getAnnotationAttributeValue(
            it, "org.springframework.data.relational.core.mapping.Table", "value")
            .map(String.class::cast)
            .map(DSL::table)
            .orElseGet(() -> DSL.table(StrUtils.toSnakeLower(it.getSimpleName()))));
    }

    @UnmodifiableView
    public static List<ColumnMeta> getColumnMetas(final Class<?> clazz) {
        return COLUMN_META_CACHE.computeIfAbsent(clazz, CrudReflection::initColumnMetas);
    }

    @UnmodifiableView
    static List<ColumnMeta> initColumnMetas(final Class<?> clazz) {
        final Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return Collections.emptyList();
        }
        final List<ColumnMeta> columnMetas = new ArrayList<>();
        for (final Field field : fields) {
            final String getterName = boolean.class.equals(field.getType()) ?
                "is" + StrUtils.toFirstCharUpper(field.getName())
                : "get" + StrUtils.toFirstCharUpper(field.getName());
            if (!ReflectionUtils.hasMethod(clazz, getterName)) {
                continue;
            }
            final boolean isId = ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.Id");
            final boolean isCreator = ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.CreatedBy");
            final boolean isCreatedDate = ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.CreatedDate");
            final boolean isUpdater = ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.LastModifiedBy");
            final boolean isUpdateDate = ReflectionUtils.isAnnotated(field,
                "org.springframework.data.annotation.LastModifiedDate");

            final Optional<String> columnOpt = ReflectionUtils.getAnnotationAttributeValue(
                field, "org.springframework.data.relational.core.mapping.Column", "value")
                .map(String.class::cast);
            final Optional<String> columnSfmOpt = ReflectionUtils.getAnnotationAttributeValue(
                field, "org.simpleflatmapper.map.annotation.Column", "value")
                .map(String.class::cast);

            final ColumnMeta columnMeta = ColumnMeta.builder().id(isId)
                .creator(isCreator).createdDate(isCreatedDate)
                .updater(isUpdater).updateDate(isUpdateDate)
                .field(field.getName())
                .getterName(getterName)
                .column(columnOpt.orElseGet(() -> columnSfmOpt.orElse(StrUtils.toSnakeLower(field.getName()))))
                .build();
            columnMetas.add(columnMeta);
        }
        return Collections.unmodifiableList(columnMetas);
    }
}
