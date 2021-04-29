package cn.javaer.snippets.security.rbac;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 用户-权限.
 *
 * @author cn-src
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserPermission implements Serializable {
    private static final long serialVersionUID = 6904897919616486183L;

    @Id
    @EqualsAndHashCode.Include
    private Long userId;

    @Id
    @EqualsAndHashCode.Include
    private Long permissionId;
}
