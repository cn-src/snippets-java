package cn.javaer.snippets.spring.data.jooq.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author cn-src
 */
@SpringJUnitConfig({SpringConfig.class})
class SimpleJooqJdbcRepositoryTest {

    @Autowired
    UserJdbcRepository userJdbcRepository;

    @Test
    void insert() {

    }
}