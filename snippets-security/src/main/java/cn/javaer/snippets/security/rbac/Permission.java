package cn.javaer.snippets.security.rbac;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 权限.
 *
 * @author cn-src
 */
@Data
@Schema(name = "权限")
public class Permission implements Serializable {
    private static final long serialVersionUID = 7277469717673633139L;
    @Id
    private Long id;

    @Schema(name = "名称")
    private String name;

    @Schema(name = "权限值")
    private String authority;
}
