package cn.javaer.snippets.security.rbac;

import cn.javaer.snippets.jooq.JdbcCrud;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static cn.javaer.snippets.security.rbac.gen.TRole.ROLE;
import static cn.javaer.snippets.security.rbac.gen.TRoleDetails.ROLE_DETAILS;
import static cn.javaer.snippets.security.rbac.gen.TRolePermission.ROLE_PERMISSION;

/**
 * @author cn-src
 */
public class RbacJdbcManager {
    private final JdbcCrud crud;
    private final Table<?> t_user = DSL.table("users");

    public RbacJdbcManager(final JdbcCrud crud, final Supplier<Long> auditor) {
        this.crud = crud;
    }

    public <T extends PersistableUser> List<T> findAllUsers(final Class<T> clazz) {
//        final List<T> users = this.dsl.selectFrom(this.t_user).fetchInto(clazz);
//        final Set<String> authorityValues = new HashSet<>();
//        this.dsl.selectFrom(this.t_user_permission).
        return null;
    }

    public List<Permission> findPermissionByUser(final PersistableUser user) {
//        this.dsl.select(
//            this.t_permission.field("id"),
//            this.t_permission.field("name"),
//            this.t_permission.field("authority")
//        ).from(this.t_permission, this.t_user_permission, this.t_role_permission)
//            .where(this.t_user_permission.field("user_id").eq(user.getId()))

//        this.dsl.selectFrom(this.t_user_permission)
        return null;
    }

    public <T extends DefaultUserDetails> List<T> findUserByEmail(final Class<T> clazz) {
        return null;//this.dsl.selectFrom(this.t_user).fetchInto(clazz);
    }

    public List<PermissionDetails> findAllPermissionDetails() {
        return null;//this.dsl.selectFrom(PERMISSION_DETAILS).fetchInto(PermissionDetails.class);
    }

    public List<RoleDetails> findAllRoleDetails() {
        return null;//this.dsl.selectFrom(ROLE_DETAILS).fetchInto(RoleDetails.class);
    }

    public void createRole(final RoleDetails roleDetails) {
        final Long id = this.crud.insert(ROLE_DETAILS, roleDetails);
        if (CollectionUtils.isNotEmpty(roleDetails.getPermissions())) {
            final List<RolePermission> rps = new ArrayList<>(roleDetails.getPermissions().size());
            for (final Permission permission : roleDetails.getPermissions()) {
                rps.add(new RolePermission(id, permission.getId()));
            }
            this.crud.batchInsert(ROLE_PERMISSION, rps);
        }
    }

    public void deleteRole(final Long id) {
        this.crud.dsl().deleteFrom(ROLE_PERMISSION)
            .where(ROLE_PERMISSION.ROLE_ID.eq(id))
            .execute();
        this.crud.dsl().deleteFrom(ROLE)
            .where(ROLE.ID.eq(id))
            .execute();
    }

    public void updateRole(final RoleDetails roleDetails) {
        this.crud.update(ROLE_DETAILS, roleDetails);
        this.crud.dsl().deleteFrom(ROLE_PERMISSION)
            .where(ROLE_PERMISSION.ROLE_ID.eq(roleDetails.getId()))
            .execute();
        if (CollectionUtils.isNotEmpty(roleDetails.getPermissions())) {
            final List<RolePermission> rps = new ArrayList<>(roleDetails.getPermissions().size());
            for (final Permission permission : roleDetails.getPermissions()) {
                rps.add(new RolePermission(roleDetails.getId(), permission.getId()));
            }
            this.crud.batchInsert(ROLE_PERMISSION, rps);
        }
    }
}
