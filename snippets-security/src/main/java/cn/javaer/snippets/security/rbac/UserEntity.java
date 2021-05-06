package cn.javaer.snippets.security.rbac;

import java.io.Serializable;
import java.util.List;

/**
 * @author cn-src
 */
public interface UserEntity extends Serializable {

    /**
     * 获取用户 ID.
     *
     * @return ID
     */
    Long getId();

    /**
     * 获取角色 ID.
     *
     * @return ID
     */
    Long getRoleId();

    /**
     * 设置角色 ID.
     *
     * @param id id
     */
    void setRoleId(Long id);

    /**
     * 获取权限值列表.
     *
     * @return 权限值列表.
     */
    List<Permission> getPermissions();

    /**
     * 设置权限值列表.
     *
     * @param permissions 权限值列表
     */
    void setPermissions(List<Permission> permissions);
}
