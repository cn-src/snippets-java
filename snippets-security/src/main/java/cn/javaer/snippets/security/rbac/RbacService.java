package cn.javaer.snippets.security.rbac;

import cn.javaer.snippets.model.Page;
import cn.javaer.snippets.model.PageParam;
import cn.javaer.snippets.security.rbac.query.QPermission;
import cn.javaer.snippets.security.rbac.query.QRole;
import io.ebean.Database;
import io.ebean.PagedList;
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
    public <U> Page<U> findUsers(Class<U> userClass, Object example, PageParam pageParam) {
        final PagedList<U> paged = db.find(userClass)
            .where().exampleLike(example)
            .setFirstRow(pageParam.getOffset())
            .setMaxRows(pageParam.getSize())
            .findPagedList();
        return Page.of(paged.getList(), paged.getTotalCount());
    }

    @Transactional(readOnly = true)
    public <U> U findUserByEmail(Class<U> userClass, String email) {
        return db.find(userClass).where().eq("email", email).findOne();
    }

    @Transactional(readOnly = true)
    public <U> U findUserByPhoneNumber(Class<U> userClass, String phoneNumber) {
        return db.find(userClass).where().eq("phoneNumber", phoneNumber).findOne();
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
        return new QRole(db).createdBy.fetch().modifiedBy.fetch().findList();
    }
}