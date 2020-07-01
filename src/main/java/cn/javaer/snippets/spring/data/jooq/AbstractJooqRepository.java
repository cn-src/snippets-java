package cn.javaer.snippets.spring.data.jooq;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectOrderByStep;
import org.jooq.SelectWhereStep;
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
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.support.ExampleMatcherAccessor;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author cn-src
 */
public abstract class AbstractJooqRepository<T, ID> {
    protected static final String AUDITOR_MUST_BE_NOT_NULL = "Auditor must be not null";
    protected final DSLContext dsl;
    protected final RelationalMappingContext context;
    protected final RelationalPersistentEntity<T> repositoryEntity;
    protected final AuditorAware<?> auditorAware;
    protected final Table<Record> repositoryTable;

    protected AbstractJooqRepository(final DSLContext dsl,
                                     final RelationalPersistentEntity<T> repositoryEntity,
                                     final RelationalMappingContext context,
                                     final AuditorAware<?> auditorAware
    ) {
        this.dsl = dsl;
        this.context = context;
        this.repositoryEntity = repositoryEntity;
        this.auditorAware = auditorAware;
        this.repositoryTable = DSL.table(repositoryEntity.getTableName().getReference());
    }

    protected Query findWithConditionStep(final Condition condition) {
        return this.dsl.selectFrom(this.repositoryTable)
                .where(condition);
    }

    protected Query findWithConditionAndPageableStep(final Condition condition, final Pageable pageable) {
        final SelectConditionStep<Record> step = this.dsl.selectFrom(this.repositoryTable)
                .where(condition);
        this.pageableStep(step, pageable);
        return step;
    }

    protected Query findWithSortStep(final Sort sort) {
        final SelectWhereStep<Record> step = this.dsl.selectFrom(this.repositoryTable);
        this.sortStep(step, sort);
        return step;
    }

    protected Query findWithPageableStep(final Pageable pageable) {
        final SelectWhereStep<Record> step = this.dsl.selectFrom(this.repositoryTable);
        this.pageableStep(step, pageable);
        return step;
    }

    protected <S extends T> SelectConditionStep<Record> findWithExampleStep(final Example<S> example) {
        final RelationalPersistentEntity<S> persistentEntity = this.getRequiredPersistentEntity(example.getProbeType());
        final Table<Record> table = DSL.table(persistentEntity.getTableName().getReference());
        final SelectConditionStep<Record> step = this.dsl.selectFrom(table).where();
        this.exampleStep(step, example, persistentEntity);
        return step;
    }

    protected <S extends T> Query findWithExampleAndSortStep(final Example<S> example, final Sort sort) {
        final SelectConditionStep<Record> query = this.findWithExampleStep(example);
        this.sortStep(query, sort);
        return query;
    }

    protected <S extends T> Query findWithExampleAndPageableStep(final Example<S> example, final Pageable pageable) {
        final SelectConditionStep<Record> query = this.findWithExampleStep(example);
        this.pageableStep(query, pageable);
        return query;
    }

    protected <S extends T> Query countWithExampleStep(final Example<S> example) {
        final RelationalPersistentEntity<S> persistentEntity = this.getRequiredPersistentEntity(example.getProbeType());
        final Table<Record> table = DSL.table(persistentEntity.getTableName().getReference());
        final SelectConditionStep<Record1<Integer>> step = this.dsl.selectCount().from(table).where();
        this.exampleStep(step, example, persistentEntity);
        return step;
    }

    protected <S extends T> void exampleStep(final SelectConditionStep<? extends Record> step, final Example<S> example, final RelationalPersistentEntity<S> persistentEntity) {
        final ExampleMatcher matcher = example.getMatcher();
        final ExampleMatcher.PropertySpecifiers propertySpecifiers = matcher.getPropertySpecifiers();
        final ExampleMatcherAccessor exampleAccessor = new ExampleMatcherAccessor(matcher);
        final DirectFieldAccessFallbackBeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(example.getProbe());

        for (final RelationalPersistentProperty persistentProperty : persistentEntity) {
            final String propertyName = persistentProperty.getName();
            if (exampleAccessor.isIgnoredPath(propertyName)) {
                continue;
            }
            final ExampleMatcher.PropertyValueTransformer transformer = exampleAccessor.getValueTransformerForPath(propertyName);
            final Optional<Object> optionalValue = transformer
                    .apply(Optional.ofNullable(beanWrapper.getPropertyValue(propertyName)));
            final String columnName = persistentProperty.getColumnName().getReference();

            Condition condition = null;
            if (optionalValue.isPresent()) {
                if (propertySpecifiers.hasSpecifierForPath(propertyName) && optionalValue.get().getClass() == String.class) {
                    final ExampleMatcher.StringMatcher stringMatcher = propertySpecifiers.getForPath(propertyName).getStringMatcher();
                    final String str = (String) optionalValue.get();
                    switch (Objects.requireNonNull(stringMatcher)) {
                        case CONTAINING:
                            if (str.length() > 0) {
                                condition = DSL.field(columnName).contains(str);
                            }
                            break;
                        case STARTING:
                            condition = DSL.field(columnName).startsWith(optionalValue.get());
                            break;
                        case ENDING:
                            condition = DSL.field(columnName).endsWith(optionalValue.get());
                            break;
                        case REGEX:
                            condition = DSL.field(columnName).likeRegex(str);
                            break;
                        case EXACT:
                            condition = DSL.field(columnName).eq(optionalValue.get());
                            break;
                        case DEFAULT:
                            if (str.length() > 0) {
                                condition = DSL.field(columnName).eq(optionalValue.get());
                            }
                            break;
                        default:
                            throw new UnsupportedOperationException(stringMatcher.name());
                    }
                }
                else {
                    if (optionalValue.get().getClass() == String.class && ((String) optionalValue.get()).length() == 0) {

                    }
                    else {
                        condition = DSL.field(columnName).eq(optionalValue.get());
                    }
                }
            }
            else if (exampleAccessor.getNullHandler().equals(ExampleMatcher.NullHandler.INCLUDE)) {
                condition = DSL.field(columnName).isNull();
            }
            else {
                continue;
            }

            if (condition == null) {
                continue;
            }
            if (matcher.isAllMatching()) {
                step.and(condition);
            }
            else {
                step.or(condition);
            }
        }
    }

    protected void pageableStep(final SelectOrderByStep<Record> step, final Pageable pageable) {
        this.sortStep(step, pageable.getSort());
        step.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    protected void sortStep(final SelectOrderByStep<Record> step, final Sort sort) {
        if (sort.isSorted()) {
            //noinspection rawtypes
            final SortField[] fields = sort.map(it -> it.isAscending() ? DSL.field(it.getProperty()).asc()
                    : DSL.field(it.getProperty()).desc()).toList().toArray(new SortField[0]);
            step.orderBy(fields);
        }
    }

    protected Query findByIdAndCreatorStep(final ID id) {
        Assert.isTrue(this.auditorAware.getCurrentAuditor().isPresent(), AUDITOR_MUST_BE_NOT_NULL);
        final String createColumnName = Objects.requireNonNull(this.repositoryEntity.getPersistentProperty(CreatedBy.class))
                .getColumnName().getReference();

        return this.dsl.selectFrom(this.repositoryTable)
                .where(DSL.field(this.repositoryEntity.getIdColumn().getReference()).eq(id))
                .and(DSL.field(createColumnName).eq(this.auditorAware.getCurrentAuditor().get()));
    }

    protected Query findAllByCreatorStep() {
        Assert.isTrue(this.auditorAware.getCurrentAuditor().isPresent(), AUDITOR_MUST_BE_NOT_NULL);
        final String createColumnName = Objects.requireNonNull(this.repositoryEntity.getPersistentProperty(CreatedBy.class))
                .getColumnName().getReference();

        return this.dsl.selectFrom(this.repositoryTable)
                .where(DSL.field(createColumnName).eq(this.auditorAware.getCurrentAuditor().get()));
    }

    protected Query findAllByCreatorStep(final Pageable pageable) {
        Assert.isTrue(this.auditorAware.getCurrentAuditor().isPresent(), AUDITOR_MUST_BE_NOT_NULL);
        final String createColumnName = Objects.requireNonNull(this.repositoryEntity.getPersistentProperty(CreatedBy.class))
                .getColumnName().getReference();

        final SelectConditionStep<Record> step = this.dsl.selectFrom(this.repositoryTable)
                .where(DSL.field(createColumnName).eq(this.auditorAware.getCurrentAuditor().get()));
        this.pageableStep(step, pageable);
        return step;
    }

    protected UpdateConditionStep<Record> updateByIdAndCreatorStep(final T instance) {
        Assert.isTrue(this.auditorAware.getCurrentAuditor().isPresent(), AUDITOR_MUST_BE_NOT_NULL);

        final Object currentAuditor = this.auditorAware.getCurrentAuditor().get();
        final UpdateSetFirstStep<Record> updateStep = this.dsl.update(this.repositoryTable);
        UpdateSetMoreStep<Record> updateStepMore = null;
        Condition idCondition = null;
        Condition createdByCondition = null;
        final RelationalPersistentEntity<?> persistentEntity = this.getRequiredPersistentEntity(instance.getClass());
        final PersistentPropertyAccessor<T> propertyAccessor = persistentEntity.getPropertyAccessor(instance);

        for (final RelationalPersistentProperty property : persistentEntity) {
            if (property.isTransient()
                    || property.isAnnotationPresent(CreatedDate.class)
                    || property.isAnnotationPresent(ReadOnlyProperty.class)) {
                continue;
            }
            final String columnName = property.getColumnName().getReference();
            if (property.isIdProperty()) {
                idCondition = DSL.field(columnName).eq(propertyAccessor.getProperty(property));
                continue;
            }
            if (property.isAnnotationPresent(CreatedBy.class)) {
                createdByCondition = DSL.field(columnName).eq(currentAuditor);
                continue;
            }
            if (property.isAnnotationPresent(LastModifiedBy.class)) {
                updateStepMore = updateStep.set(DSL.field(columnName), currentAuditor);
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

    protected DeleteConditionStep<Record> deleteByIdAndCreatorStep(final ID id) {
        Assert.notNull(id, "Id must not be null");
        Assert.isTrue(this.auditorAware.getCurrentAuditor().isPresent(), AUDITOR_MUST_BE_NOT_NULL);

        final String createColumnName = Objects.requireNonNull(this.repositoryEntity.getPersistentProperty(CreatedBy.class))
                .getColumnName().getReference();

        return this.dsl.deleteFrom(this.repositoryTable)
                .where(DSL.field(this.repositoryEntity.getIdColumn().getReference()).eq(id))
                .and(DSL.field(createColumnName).eq(this.auditorAware.getCurrentAuditor().get()));
    }

    @SuppressWarnings("unchecked")
    protected <S> RelationalPersistentEntity<S> getRequiredPersistentEntity(final Class<S> domainType) {
        return (RelationalPersistentEntity<S>) this.context.getRequiredPersistentEntity(domainType);
    }
}
