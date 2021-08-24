package cn.javaer.snippets.security.rbac;

import cn.hutool.core.collection.CollUtil;
import cn.javaer.snippets.jooq.JdbcCrud;
import cn.javaer.snippets.model.Page;
import cn.javaer.snippets.model.PageParam;
import org.jooq.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final UserTableMetaProvider userMeta;

    public RbacJdbcManager(final JdbcCrud crud, final UserTableMetaProvider userMeta) {
        this.crud = crud;
        this.userMeta = userMeta;
    }

    public <T extends UserEntity> Page<T> findAllUsers(final PageParam pageParam) {
        return this.crud.findAll(this.userMeta.meta(), pageParam);
    }

    public <T extends UserEntity> Optional<T> findUserByEmail(final String email) {
        final Condition condition = this.userMeta.meta().emailField().eq(email);
        final Optional<T> userOpt = this.crud.findOne(this.userMeta.meta(), condition);
        userOpt.ifPresent(u -> u.setPermissions(this.findPermissionByUser(u)));
        return userOpt;
    }

    public <T extends UserEntity> Optional<T> findUserByMobile(final String mobile) {
        final Condition condition = this.userMeta.meta().mobileField().eq(mobile);
        final Optional<T> userOpt = this.crud.findOne(this.userMeta.meta(), condition);
        userOpt.ifPresent(u -> u.setPermissions(this.findPermissionByUser(u)));
        return userOpt;
    }

    // TODO 无法查出数据
    public List<Permission> findPermissionByUser(final UserEntity user) {
        return this.crud.dsl().select(PERMISSION_FIELDS)
            .from(PERMISSION, USER_PERMISSION, ROLE_PERMISSION)
            .where(USER_PERMISSION.USER_ID.eq(user.getId()).and(USER_PERMISSION.PERMISSION_ID.eq(PERMISSION.ID)))
            .or(ROLE_PERMISSION.ROLE_ID.eq(user.getRoleId()).and(ROLE_PERMISSION.PERMISSION_ID.eq(PERMISSION.ID)))
            .fetchInto(Permission.class)
            .stream().distinct().collect(Collectors.toList());
    }

    public List<Permission> findAllPermissions() {
        return this.crud.findAll(PERMISSION);
    }

    public Page<PermissionDetails> findAllPermissionDetails(final PageParam pageParam) {
        return this.crud.findAll(PERMISSION_DETAILS, pageParam);
    }

    public List<Role> findAllRoles() {
        return this.crud.findAll(ROLE);
    }

    public Page<RoleDetails> findAllRoleDetails(final PageParam pageParam) {
        return this.crud.findAll(ROLE_DETAILS, pageParam);
    }

    public void createRole(final RoleDetails roleDetails) {
        final Long id = this.crud.insert(ROLE_DETAILS, roleDetails);
        if (CollUtil.isNotEmpty(roleDetails.getPermissions())) {
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
        if (CollUtil.isNotEmpty(roleDetails.getPermissions())) {
            final List<RolePermission> rps = new ArrayList<>(roleDetails.getPermissions().size());
            for (final Permission permission : roleDetails.getPermissions()) {
                rps.add(new RolePermission(roleDetails.getId(), permission.getId()));
            }
            this.crud.batchInsert(ROLE_PERMISSION, rps);
        }
    }
}