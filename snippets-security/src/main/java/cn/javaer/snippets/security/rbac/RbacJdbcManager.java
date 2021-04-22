package cn.javaer.snippets.security.rbac;

import org.jooq.DSLContext;
import org.jooq.InsertValuesStepN;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author cn-src
 */
public class RbacJdbcManager {
    private final DSLContext dsl;
    private final Table<?> t_user = DSL.table("users");
    private final Table<?> t_role = DSL.table("role");
    private final Table<?> t_permission = DSL.table("permission");
    private final Table<?> t_role_permission = DSL.table("role_permission");
    private final Supplier<Long> auditor;

    public RbacJdbcManager(final DSLContext dsl, final Supplier<Long> auditor) {
        this.dsl = dsl;
        this.auditor = auditor;
    }

    public <T extends DefaultUserDetails> List<T> findAllUsers(final Class<T> clazz) {
        return this.dsl.selectFrom(this.t_user).fetchInto(clazz);
    }

    public <T extends DefaultUserDetails> List<T> findUserByEmail(final Class<T> clazz) {
        return this.dsl.selectFrom(this.t_user).fetchInto(clazz);
    }

    public List<Permission> findAllPermission() {
        return this.dsl.selectFrom(this.t_permission).fetchInto(Permission.class);
    }

    public List<Role> findAllRole() {
        return this.dsl.selectFrom(this.t_role).fetchInto(Role.class);
    }

    public void createRole(final Role role) {
        final LocalDateTime now = LocalDateTime.now();
        final Long roleId = Objects.requireNonNull(this.dsl.insertInto(this.t_role)
            .set(DSL.field("name"), role.getName())
            .set(DSL.field("description"), role.getDescription())
            .set(DSL.field("created_date"), now)
            .set(DSL.field("created_by_id"), this.auditor.get())
            .returningResult(DSL.field("id", Long.class))
            .fetchOne()).value1();
        this.savePermissions(role, now, roleId);
    }

    public void deleteRole(final Long id) {
        this.dsl.deleteFrom(this.t_role_permission).where(DSL.field("role_id").eq(id))
            .execute();
        this.dsl.deleteFrom(this.t_role).where(DSL.field("id").eq(id))
            .execute();
    }

    public void updateRole(final Role role) {
        this.dsl.update(this.t_role)
            .set(DSL.field("name"), role.getName())
            .set(DSL.field("description"), role.getDescription())
            .where(DSL.field("id").eq(role.getId()))
            .execute();
        this.dsl.deleteFrom(this.t_role_permission).where(DSL.field("role_id").eq(role.getId()))
            .execute();
        this.savePermissions(role, LocalDateTime.now(), role.getId());
    }

    private void savePermissions(final Role role,
                                 final LocalDateTime now, final Long roleId) {
        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            final InsertValuesStepN<?> step =
                this.dsl.insertInto(this.t_role_permission).columns(Arrays.asList(
                    DSL.field("role_id"),
                    DSL.field("permission_id"),
                    DSL.field("created_date"),
                    DSL.field("created_by_id")));
            for (final Permission permission : role.getPermissions()) {
                step.values(roleId, permission.getId(), now, this.auditor.get());
            }
        }
    }
}
