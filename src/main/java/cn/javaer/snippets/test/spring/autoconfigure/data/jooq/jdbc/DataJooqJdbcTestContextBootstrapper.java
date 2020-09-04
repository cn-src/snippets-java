package cn.javaer.snippets.test.spring.autoconfigure.data.jooq.jdbc;

import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.test.context.TestContextBootstrapper;

/**
 * {@link TestContextBootstrapper} for {@link DataJooqJdbcTest @DataJooqJdbcTest} support.
 *
 * @author cn-src
 */
class DataJooqJdbcTestContextBootstrapper extends SpringBootTestContextBootstrapper {

    @Override
    protected String[] getProperties(final Class<?> testClass) {
        return MergedAnnotations.from(testClass, SearchStrategy.INHERITED_ANNOTATIONS).get(DataJooqJdbcTest.class)
            .getValue("properties", String[].class).orElse(null);
    }
}
