package cn.javaer.snippets.security.rbac;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色.
 *
 * @author cn-src
 */
@Data
@Schema(name = "角色详情")
@Table("role")
public class RoleDetails implements Serializable {
    private static final long serialVersionUID = -388063889316870772L;

    @Id
    private Long id;

    @Schema(name = "名称")
    private String name;

    @Schema(name = "描述")
    private String description;

    @Transient
    @Schema(name = "权限列表")
    private List<Permission> permissions;

    @CreatedDate
    @Schema(name = "创建时间")
    private LocalDateTime createdDate;

    @CreatedBy
    @Schema(name = "创建者 ID")
    private Long createdById;
}
