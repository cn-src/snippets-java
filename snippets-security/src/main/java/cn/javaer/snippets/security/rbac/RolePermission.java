package cn.javaer.snippets.security.rbac;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 角色-权限.
 *
 * @author cn-src
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 6904897919616486183L;

    @Id
    private Long roleId;

    @Id
    private Long permissionId;
}
