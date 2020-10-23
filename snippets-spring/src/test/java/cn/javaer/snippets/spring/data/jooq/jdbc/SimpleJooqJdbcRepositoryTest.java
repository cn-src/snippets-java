package cn.javaer.snippets.spring.data.jooq.jdbc;

import org.jooq.DSLContext;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.jooq.impl.DSL.primaryKey;

/**
 * @author cn-src
 */
@SpringJUnitConfig({UserJdbcRepository.class, SpringConfig.class})
class SimpleJooqJdbcRepositoryTest {
    @Autowired DSLContext dsl;

    @Autowired UserJdbcRepository userJdbcRepository;

    @BeforeEach
    void setUp() {
        this.dsl.createTableIfNotExists("users")
            .column("ID", SQLDataType.BIGINT.nullable(false).identity(true))
            .column("NAME", SQLDataType.VARCHAR(50))
            .column("CREATED_BY_ID", SQLDataType.BIGINT)
            .column("CREATED_DATE", SQLDataType.TIMESTAMP)
            .constraints(primaryKey("ID"))
            .execute();
    }

    @Test
    void insert() {
        this.userJdbcRepository.insert(User.builder()
            .id(1L)
            .name("n1")
            .build());
        final User user = this.userJdbcRepository.findById(1L).orElse(null);
        System.out.println(user);
    }
}