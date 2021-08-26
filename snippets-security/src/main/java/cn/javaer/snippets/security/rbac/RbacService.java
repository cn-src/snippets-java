package cn.javaer.snippets.security.rbac;

import cn.javaer.snippets.security.rbac.query.QPermission;
import cn.javaer.snippets.security.rbac.query.QRole;
import io.ebean.Database;
import io.ebean.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author cn-src
 */
@Transactional
public class RbacService {

    private final Database db;

    public RbacService(Database db) {
        this.db = Objects.requireNonNull(db);
    }

    @Transactional(readOnly = true)
    public List<Permission> findPermissions() {
        return new QPermission().findList();
    }

    public void saveRole(Role role) {
        db.save(role);
    }

    public void deleteRole(Long roleId) {
        db.delete(Role.class, roleId);
    }

    @Transactional(readOnly = true)
    public List<Role> findRoles() {
        return new QRole(db)
            .createdBy.fetch()
            .modifiedBy.fetch()
            .findList();
    }
}