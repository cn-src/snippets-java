package cn.javaer.snippets.test.spring.autoconfigure.data.jooq.jdbc;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link ImportAutoConfiguration Auto-configuration imports} for typical Data jOOQ JDBC tests.
 * Most tests should consider using {@link DataJooqJdbcTest @DataJooqJdbcTest} rather than using
 * this annotation directly.
 *
 * @author cn-src
 * @see DataJooqJdbcTest
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration
public @interface AutoConfigureDataJooqJdbc {

}
