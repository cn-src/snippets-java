package cn.javaer.snippets.spring.data.jooq.jdbc;

import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.impl.DSL.primaryKey;

/**
 * @author cn-src
 */
@SpringJUnitConfig({UserJdbcRepository.class, SpringConfig.class})
class SimpleJooqJdbcRepositoryTest {
    @Autowired DSLContext dsl;

    @Autowired UserJdbcRepository userJdbcRepository;

    @BeforeAll
    static void beforeAll() {
        SpringConfig.container.start();
    }

    @AfterAll
    static void afterAll() {
        SpringConfig.container.stop();
    }

    @BeforeEach
    void setUp() {
        this.dsl.dropTableIfExists("users");
        this.dsl.createTable("users")
            .column("id", SQLDataType.BIGINT.nullable(false).identity(true))
            .column("name", SQLDataType.VARCHAR(50))
            .column("jsonb1", SQLDataType.JSONB)
            .column("created_by_id", SQLDataType.BIGINT)
            .column("created_date", SQLDataType.TIMESTAMP)
            .constraints(primaryKey("id"))
            .execute();
    }

    @Test
    void insert() {
        final JSONB jsonb1 = JSONB.valueOf("{\"k\": 1}");
        this.userJdbcRepository.insert(User.builder()
            .id(1L)
            .name("n1")
            .jsonb1(jsonb1)
            .build());
        final User user = this.userJdbcRepository.findById(1L).orElse(null);
        assertThat(user).extracting(User::getId).isEqualTo(1L);
        assertThat(user).extracting(User::getJsonb1).isEqualTo(jsonb1);
    }

    @Test
    void update() {
        final JSONB jsonb1 = JSONB.valueOf("{\"k\": 1}");
        final User instance = User.builder()
            .id(1L)
            .name("n1")
            .jsonb1(jsonb1)
            .build();
        this.userJdbcRepository.insert(instance);
        instance.setName("nn");
        final User user = this.userJdbcRepository.updateByIdAndCreator(instance);
        assertThat(user).extracting(User::getId).isEqualTo(1L);
        assertThat(user).extracting(User::getName).isEqualTo("nn");
        assertThat(user).extracting(User::getJsonb1).isEqualTo(jsonb1);
    }
}