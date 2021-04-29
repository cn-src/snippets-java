package cn.javaer.snippets.security.rbac;

import cn.javaer.snippets.jooq.JdbcCrud;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cn.javaer.snippets.security.rbac.gen.TPermission.PERMISSION;
import static cn.javaer.snippets.security.rbac.gen.TPermission.PERMISSION_FIELDS;
import static cn.javaer.snippets.security.rbac.gen.TPermissionDetails.PERMISSION_DETAILS;
import static cn.javaer.snippets.security.rbac.gen.TRole.ROLE;
import static cn.javaer.snippets.security.rbac.gen.TRoleDetails.ROLE_DETAILS;
import static cn.javaer.snippets.security.rbac.gen.TRolePermission.ROLE_PERMISSION;
import static cn.javaer.snippets.security.rbac.gen.TUserPermission.USER_PERMISSION;

/**
 * @author cn-src
 */
public class RbacJdbcManager {
    private final JdbcCrud crud;
    private final Table<?> t_user = DSL.table("users");

    public RbacJdbcManager(final JdbcCrud crud) {
        this.crud = crud;
    }

    public <T extends PersistableUser> List<T> findAllUsers(final Class<T> clazz) {
        return this.crud.dsl().selectFrom(this.t_user).fetchInto(clazz);
    }

    public <T extends PersistableUser>
    Optional<T> findUserByEmail(final Class<T> clazz, final String email) {
        final T user = this.crud.dsl().selectFrom(this.t_user)
            .where(DSL.field("email").eq(email))
            .fetchOneInto(clazz);
        final Optional<T> userOpt = Optional.ofNullable(user);
        userOpt.ifPresent(u -> u.setPermissions(this.findPermissionByUser(u)));
        return userOpt;
    }

    public <T extends PersistableUser>
    Optional<T> findUserByMobile(final Class<T> clazz, final String mobile) {
        final T user = this.crud.dsl().selectFrom(this.t_user)
            .where(DSL.field("mobile").eq(mobile))
            .fetchOneInto(clazz);
        final Optional<T> userOpt = Optional.ofNullable(user);
        userOpt.ifPresent(u -> u.setPermissions(this.findPermissionByUser(u)));
        return userOpt;
    }

    public List<Permission> findPermissionByUser(final PersistableUser user) {
        return this.crud.dsl().select(PERMISSION_FIELDS)
            .from(PERMISSION, USER_PERMISSION, ROLE_PERMISSION)
            .where(USER_PERMISSION.USER_ID.eq(user.getId()).and(USER_PERMISSION.PERMISSION_ID.eq(PERMISSION.ID)))
            .or(ROLE_PERMISSION.ROLE_ID.eq(user.getRoleId()).and(ROLE_PERMISSION.PERMISSION_ID.eq(PERMISSION.ID)))
            .fetchInto(Permission.class);
    }

    public List<PermissionDetails> findAllPermissionDetails() {
        return this.crud.findAll(PERMISSION_DETAILS);
    }

    public List<RoleDetails> findAllRoleDetails() {
        return this.crud.dsl().selectFrom(ROLE_DETAILS).fetchInto(RoleDetails.class);
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
