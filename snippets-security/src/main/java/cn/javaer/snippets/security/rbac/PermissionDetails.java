package cn.javaer.snippets.security.rbac;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Schema(name = "权限详情")
@Table("permission")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PermissionDetails implements Serializable {
    private static final long serialVersionUID = -219244584579949608L;
    @Id
    private Long id;

    @Schema(name = "名称")
    private String name;

    @Schema(name = "分组")
    private String group;

    @EqualsAndHashCode.Include
    @Schema(name = "权限值")
    private String authority;

    @Schema(name = "描述")
    private String description;

    @CreatedBy
    @Schema(name = "创建者 ID")
    private Long createdById;

    @CreatedDate
    @Schema(name = "创建时间")
    private LocalDateTime createdDate;
}