package cn.javaer.snippets.easybatch;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * @author cn-src
 */
public class TestDataSource {
    @Bean
    public DataSource dataSource() {
        return create();
    }

    public static DataSource create() {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        return dataSource;
    }
}
