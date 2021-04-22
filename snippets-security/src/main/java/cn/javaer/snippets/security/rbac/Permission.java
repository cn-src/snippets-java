package cn.javaer.snippets.security.rbac;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限.
 *
 * @author cn-src
 */
@Data
@Schema(name = "权限")
public class Permission implements Serializable {
    private static final long serialVersionUID = 7277469717673633139L;
    private Long id;

    @Schema(name = "名称")
    private String name;

    @Schema(name = "权限值")
    private String authority;

    @Schema(name = "描述")
    private String description;

    @Schema(name = "创建时间")
    private LocalDateTime createdDate;

    @Schema(name = "创建者 ID")
    private Long createdById;
}
