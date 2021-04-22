package cn.javaer.snippets.security.rbac;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

/**
 * 角色.
 *
 * @author cn-src
 */
@Data
@FieldDefaults
@Schema(name = "角色")
public class Role implements Serializable {
    private static final long serialVersionUID = -388063889316870772L;
    private Long id;

    @Schema(name = "名称")
    private String name;

    @Schema(name = "描述")
    private String description;

    @Schema(name = "权限列表")
    private List<Permission> permissions;
}
