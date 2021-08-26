package cn.javaer.snippets.security.rbac;

import cn.javaer.snippets.model.Page;
import cn.javaer.snippets.model.PageParam;
import io.ebean.DB;
import io.ebean.Database;
import io.ebean.test.UserContext;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class RbacServiceTest {
    Database db = DB.getDefault();
    RbacService rbacService = new RbacService(db);

    @BeforeEach
    void setUp() {
        UserContext.reset();
        db.beginTransaction();
    }

    @AfterEach
    void tearDown() {
        db.rollbackTransaction();
    }

    @Data
    static class UserQuery {
        private String name;
    }

    @Test
    void testUser() {
        db.saveAll(new UserLite("u1"), new UserLite("u2"), new UserLite("u3"), new UserLite("u4"));
        final Page<UserLite> users = rbacService.findUsers(UserLite.class,
            new UserQuery(), PageParam.of(2, 2));
        System.out.println(users);
    }

    @Test
    @DisplayName("测试角色，包括及联保存")
    void testRole() {
        final UserLite user = new UserLite(1L, "user1");
        db.save(user);
        UserContext.setUserId(user);
        final Role.RoleBuilder bd = Role.builder()
            .name("role1")
            .permission(Permission.builder()
                .name("p1")
                .authority("pv1")
                .build());
        rbacService.saveRole(bd.build());

        final List<Role> roles = rbacService.findRoles();
        assertThat(roles).hasSize(1);
        assertThat(roles.get(0).getPermissions()).hasSize(1);
        assertThat(roles.get(0).getCreatedBy()).isNotNull();
    }
}