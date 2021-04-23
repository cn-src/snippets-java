package cn.javaer.snippets.security.rbac;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 角色.
 *
 * @author cn-src
 */
@Data
@Schema(name = "角色")
public class Role implements Serializable {
    private static final long serialVersionUID = -388063889316870772L;

    @Id
    private Long id;

    @Schema(name = "名称")
    private String name;
}
