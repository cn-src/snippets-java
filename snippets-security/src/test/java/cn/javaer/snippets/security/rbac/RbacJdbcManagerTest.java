package cn.javaer.snippets.security.rbac;

import cn.javaer.snippets.jooq.JdbcCrud;
import cn.javaer.snippets.model.Page;
import cn.javaer.snippets.model.PageParam;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

/**
 * @author cn-src
 */
class RbacJdbcManagerTest {
    final static PostgreSQLContainer<?> container =
        new PostgreSQLContainer<>(DockerImageName.parse("postgis/postgis:10-2.5-alpine")
            .asCompatibleSubstituteFor("postgres"))
            .withInitScript("rbac.ddl")
            .withDatabaseName(RbacJdbcManagerTest.class.getSimpleName());
    RbacJdbcManager rbacJdbcManager = new RbacJdbcManager(new JdbcCrud(this.dataSource()),
        new UserTableMetaProvider() {
            @Override
            public <T extends UserEntity> UserTableMeta<T, ?, ?> meta() {
                return null;
            }
        });

    @Test
    void findAllPermissionDetails() {
        final Page<PermissionDetails> permissionDetails =
            this.rbacJdbcManager.findAllPermissionDetails(PageParam.of(1, 10));
        System.out.println(permissionDetails);
    }

    DataSource dataSource() {
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUser(container.getUsername());
        dataSource.setPassword(container.getPassword());
        return dataSource;
    }

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }
}