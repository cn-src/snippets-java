package cn.javaer.snippets.test.spring.autoconfigure.data.jooq.jdbc;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.test.autoconfigure.filter.StandardAnnotationCustomizableTypeExcludeFilter;

/**
 * {@link TypeExcludeFilter} for {@link DataJooqJdbcTest @DataJooqJdbcTest}.
 *
 * @author cn-src
 */
public final class DataJooqJdbcTypeExcludeFilter extends StandardAnnotationCustomizableTypeExcludeFilter<DataJooqJdbcTest> {

    @SuppressWarnings("unused")
    DataJooqJdbcTypeExcludeFilter(final Class<?> testClass) {
        super(testClass);
    }
}
