package cn.javaer.snippets.spring.data.jooq.jdbc;

import cn.javaer.snippets.spring.data.jooq.jdbc.config.EnableJooqJdbcRepositories;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@EnableJooqJdbcRepositories
@EnableJdbcAuditing
class SpringConfig extends AbstractJdbcConfiguration {

    final static PostgreSQLContainer<?> container =
        new PostgreSQLContainer<>(DockerImageName.parse("postgis/postgis:10-2.5-alpine")
            .asCompatibleSubstituteFor("postgres"))
            .withDatabaseName(SimpleJooqJdbcRepositoryTest.class.getSimpleName());

    @Bean
    AuditorAware<Long> auditorAware() {
        return () -> Optional.of(999L);
    }

    @Bean
    DataSource dataSource() {
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUser(container.getUsername());
        dataSource.setPassword(container.getPassword());
        return dataSource;
    }

    @Bean
    DSLContext dslContext() {
        return DSL.using(this.dataSource(), SQLDialect.POSTGRES);
    }

    @Bean
    NamedParameterJdbcOperations namedParameterJdbcOperations(final DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    TransactionManager transactionManager(final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @NonNull
    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(JsonbConverters.getConvertersToRegister());
    }
}