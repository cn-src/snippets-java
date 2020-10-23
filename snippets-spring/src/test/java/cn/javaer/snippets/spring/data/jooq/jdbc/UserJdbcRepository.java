package cn.javaer.snippets.spring.data.jooq.jdbc;

import org.springframework.stereotype.Repository;

@Repository
interface UserJdbcRepository extends JooqJdbcRepository<User, Long> {

}
