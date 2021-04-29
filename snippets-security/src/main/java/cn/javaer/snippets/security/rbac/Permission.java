package cn.javaer.snippets.security.rbac;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 权限.
 *
 * @author cn-src
 */
@Data
@Builder
@Schema(name = "权限")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Permission implements Serializable {
    private static final long serialVersionUID = 7277469717673633139L;
    @Id
    private Long id;

    @Schema(name = "名称")
    private String name;

    @Schema(name = "分组")
    private String group;

    @Schema(name = "权限值")
    @EqualsAndHashCode.Include
    private String authority;
}
