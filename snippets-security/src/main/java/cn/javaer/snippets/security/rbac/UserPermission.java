package cn.javaer.snippets.security.rbac;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户-权限.
 *
 * @author cn-src
 */
@Data
public class UserPermission implements Serializable {
    private static final long serialVersionUID = 6904897919616486183L;
    private Long userId;
    private String authority;
}
