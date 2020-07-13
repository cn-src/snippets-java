package cn.javaer.snippets.spring.data.jooq;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectLimitPercentAfterOffsetStep;
import org.jooq.SelectOrderByStep;
import org.jooq.SelectSeekStepN;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
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
import java.util.Objects;

/**
 * @author cn-src
 */
public class StepUtils {
    private StepUtils() {
    }

    public static SelectSeekStepN<?> sortStep(final SelectOrderByStep<?> step, final Sort sort) {
        //noinspection rawtypes
        final SortField[] fields = sort.map(it -> it.isAscending() ? DSL.field(it.getProperty()).asc()
                : DSL.field(it.getProperty()).desc()).toList().toArray(new SortField[0]);
        return step.orderBy(fields);
    }

    public static SelectLimitPercentAfterOffsetStep<?> pageableStep(final SelectOrderByStep<?> step, final Pageable pageable) {
        final Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            return sortStep(step, sort).offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        }
        return step.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    public static UpdateConditionStep<?> updateByIdAndCreatorStep(
            final DSLContext dsl, final Table<Record> table,
            final RelationalPersistentEntity<?> persistentEntity, final Object auditor,
            final Object instance) {

        final UpdateSetFirstStep<Record> updateStep = dsl.update(table);
        UpdateSetMoreStep<Record> updateStepMore = null;
        Condition idCondition = null;
        Condition createdByCondition = null;
        final PersistentPropertyAccessor<?> propertyAccessor = persistentEntity.getPropertyAccessor(instance);

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
                updateStepMore = updateStep.set(DSL.field(columnName), auditor);
                continue;
            }
            if (property.isAnnotationPresent(LastModifiedDate.class)) {
                updateStepMore = updateStep.set(DSL.field(columnName), LocalDateTime.now());
                continue;
            }
            updateStepMore = updateStep.set(DSL.field(columnName), propertyAccessor.getProperty(property));
        }
        return Objects.requireNonNull(updateStepMore).where(Objects.requireNonNull(idCondition).and(Objects.requireNonNull(createdByCondition)));
    }
}
