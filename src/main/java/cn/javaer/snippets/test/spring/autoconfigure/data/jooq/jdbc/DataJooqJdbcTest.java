package cn.javaer.snippets.test.spring.autoconfigure.data.jooq.jdbc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.env.Environment;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that can be used for a Data jOOQ JDBC test that focuses <strong>only</strong> on
 * Data jOOQ JDBC components.
 * <p>
 * Using this annotation will disable full auto-configuration and instead apply only
 * configuration relevant to Data jOOQ JDBC tests.
 * <p>
 * By default, tests annotated with {@code @DataJdbcTest} are transactional and roll back
 * at the end of each test. They also use an embedded in-memory database (replacing any
 * explicit or usually auto-configured DataSource). The
 * {@link AutoConfigureTestDatabase @AutoConfigureTestDatabase} annotation can be used to
 * override these settings.
 * <p>
 * If you are looking to load your full application configuration, but use an embedded
 * database, you should consider {@link SpringBootTest @SpringBootTest} combined with
 * {@link AutoConfigureTestDatabase @AutoConfigureTestDatabase} rather than this
 * annotation.
 * <p>
 * When using JUnit 4, this annotation should be used in combination with
 * {@code @RunWith(SpringRunner.class)}.
 *
 * @author cn-src
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(DataJooqJdbcTestContextBootstrapper.class)
@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(DataJooqJdbcTypeExcludeFilter.class)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJooqJdbc
@AutoConfigureTestDatabase
@ImportAutoConfiguration
public @interface DataJooqJdbcTest {

    /**
     * Properties in form {@literal key=value} that should be added to the Spring
     * {@link Environment} before the test runs.
     *
     * @return the properties to add
     */
    String[] properties() default {};

    /**
     * Determines if default filtering should be used with
     * {@link SpringBootApplication @SpringBootApplication}. By default no beans are
     * included.
     *
     * @return if default filters should be used
     *
     * @see #includeFilters()
     * @see #excludeFilters()
     */
    boolean useDefaultFilters() default true;

    /**
     * A set of include filters which can be used to add otherwise filtered beans to the
     * application context.
     *
     * @return include filters to apply
     */
    Filter[] includeFilters() default {};

    /**
     * A set of exclude filters which can be used to filter beans that would otherwise be
     * added to the application context.
     *
     * @return exclude filters to apply
     */
    Filter[] excludeFilters() default {};

    /**
     * Auto-configuration exclusions that should be applied for this test.
     *
     * @return auto-configuration exclusions to apply
     */
    @AliasFor(annotation = ImportAutoConfiguration.class, attribute = "exclude")
    Class<?>[] excludeAutoConfiguration() default {};
}
