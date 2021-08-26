package cn.javaer.snippets.security.rbac;

import io.ebean.DB;
import io.ebean.Database;
import io.ebean.test.UserContext;
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

    @Test
    @DisplayName("测试保存，包括及联保存")
    void save() {
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