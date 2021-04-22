package cn.javaer.snippets.security.rbac;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色-权限.
 *
 * @author cn-src
 */
@Data
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 6904897919616486183L;
    private Long roleId;
    private String authority;
}
