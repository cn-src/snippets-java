package cn.javaer.snippets.security.rbac;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author cn-src
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Schema(name = "权限")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @Schema(name = "名称")
    @Column(length = 50, nullable = false)
    private String name;

    @Schema(name = "分类")
    @Column(length = 50)
    private String category;

    @Column(length = 100, nullable = false, unique = true)
    @Schema(name = "权限值")
    private String authority;

    @Schema(name = "描述")
    @Column(length = 500)
    private String description;
}