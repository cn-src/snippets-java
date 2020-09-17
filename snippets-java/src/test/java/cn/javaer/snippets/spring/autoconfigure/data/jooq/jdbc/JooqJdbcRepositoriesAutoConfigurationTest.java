package cn.javaer.snippets.spring.autoconfigure.data.jooq.jdbc;

import cn.javaer.snippets.TestAutoConfigurationPackage;
import cn.javaer.snippets.TestContainer;
import cn.javaer.snippets.empty.EmptyDataPackage;
import cn.javaer.snippets.spring.data.jooq.jdbc.config.EnableJooqJdbcRepositories;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.Assertions;
import org.jooq.JSONB;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

/**
 * @author cn-src
 */
@Testcontainers
class JooqJdbcRepositoriesAutoConfigurationTest {

    @Container
    private final static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("mdillon/postgis:10-alpine")
            .withDatabaseName(JooqJdbcRepositoriesAutoConfigurationTest.class.getSimpleName());
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(JooqJdbcRepositoriesAutoConfiguration.class,
                    JooqAutoConfiguration.class,
                    JdbcTemplateAutoConfiguration.class,
                    DataSourceAutoConfiguration.class,
                    DataSourceTransactionManagerAutoConfiguration.class));

    @Test
    void jsonbTest() {
        this.contextRunner
                .withUserConfiguration(EnableRepositoriesConfiguration.class)
                .run(context -> {
                    JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
                    //language=PostgreSQL
                    jdbcTemplate.execute("CREATE TABLE demo1 (id bigserial NOT NULL" +
                            " CONSTRAINT demo1_pkey" +
                            " PRIMARY KEY, jsonb1 jsonb,jsonb2 jsonb)");
                    DemoRepository repository = context.getBean(DemoRepository.class);
                    //language=JSON
                    JSONB jsonb1 = JSONB.valueOf("{\"k1\":1}");
                    ObjectNode jsonb2 = new ObjectNode(new JsonNodeFactory(true));
                    jsonb2.put("k2", "v2");
                    repository.save(new Demo1(jsonb1, jsonb2));
                    Iterable<Demo1> demos = repository.findAll();
                    Assertions.assertThat(demos).hasSize(1);
                });
    }

    @TestAutoConfigurationPackage(EmptyDataPackage.class)
    @EnableJooqJdbcRepositories(basePackageClasses = DemoRepository.class)
    static class EnableRepositoriesConfiguration {
        @Bean
        DataSource dataSource() {
            return TestContainer.createDataSource(container);
        }
    }
}