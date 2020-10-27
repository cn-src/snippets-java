package cn.javaer.snippets.spring.data.jooq;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectLimitPercentAfterOffsetStep;
import org.jooq.SelectOrderByStep;
import org.jooq.SelectSeekStepN;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.Update;
import org.jooq.UpdateSetMoreStep;
import org.jooq.UpdateSetStep;
import org.jooq.impl.DSL;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author cn-src
 */
public class StepUtils {
    private StepUtils() {
    }

    public static Field<?>[] getFields(final RelationalPersistentEntity<?> persistentEntity) {
        final List<Field<?>> fields = new ArrayList<>();
        for (final RelationalPersistentProperty property : persistentEntity) {
            if (!property.isTransient()) {
                fields.add(DSL.field(property.getColumnName().getReference()));
            }
        }
        return fields.toArray(new Field[0]);
    }

    public static SelectSeekStepN<?> sortStep(final RelationalPersistentEntity<?> persistentEntity,
                                              final SelectOrderByStep<?> step, final Sort sort) {

        @SuppressWarnings("rawtypes") final SortField[] fields = sort.map(it -> {
            final String colName = persistentEntity.getRequiredPersistentProperty(it.getProperty())
                .getColumnName().getReference();
            return it.isAscending() ? DSL.field(colName).asc() : DSL.field(colName).desc();
        }).toList().toArray(new SortField[0]);
        return step.orderBy(fields);
    }

    public static SelectLimitPercentAfterOffsetStep<?> pageableStep(
        final RelationalPersistentEntity<?> persistentEntity,
        final SelectOrderByStep<?> step, final Pageable pageable) {

        final Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            return sortStep(persistentEntity, step, sort).offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        }
        return step.offset(pageable.getOffset())
            .limit(pageable.getPageSize());
    }

    public static Update<?> updateByIdAndCreatorStep(
        final DSLContext dsl, final Table<Record> table,
        final RelationalPersistentEntity<?> persistentEntity, final Object auditor,
        final Object instance) {

        UpdateSetStep<?> updateStep = dsl.update(table);
        Condition idCondition = null;
        Condition createdByCondition = null;
        final PersistentPropertyAccessor<?> propertyAccessor =
            persistentEntity.getPropertyAccessor(instance);

        for (final RelationalPersistentProperty property : persistentEntity) {
            if (property.isTransient() || property.isAnnotationPresent(CreatedDate.class)) {
                continue;
            }
            final String columnName = property.getColumnName().getReference();
            if (property.isIdProperty()) {
                idCondition = DSL.field(columnName).eq(propertyAccessor.getProperty(property));
                continue;
            }
            if (property.isAnnotationPresent(CreatedBy.class)) {
                createdByCondition = DSL.field(columnName).eq(auditor);
                continue;
            }
            if (property.isAnnotationPresent(LastModifiedBy.class)) {
                updateStep = updateStep.set(DSL.field(columnName), auditor);
                continue;
            }
            if (property.isAnnotationPresent(LastModifiedDate.class)) {
                updateStep = updateStep.set(DSL.field(columnName), LocalDateTime.now());
                continue;
            }
            updateStep = updateStep.set(DSL.field(columnName),
                propertyAccessor.getProperty(property));
        }
        return Objects.requireNonNull((UpdateSetMoreStep<?>) updateStep)
            .where(Objects.requireNonNull(idCondition)
                .and(Objects.requireNonNull(createdByCondition)));
    }
}
