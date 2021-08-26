package cn.javaer.snippets.security.rbac;

import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import io.ebean.annotation.WhoCreated;
import io.ebean.annotation.WhoModified;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色.
 *
 * @author cn-src
 */
@Data
@Entity
@Builder
@Schema(name = "角色")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @Schema(name = "名称")
    @Column(length = 50, nullable = false)
    private String name;

    @Schema(name = "描述")
    @Column(length = 500)
    private String description;

    @Singular
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @Schema(name = "权限列表")
    private List<Permission> permissions;

    @WhoModified
    @Schema(name = "修改者")
    @ManyToOne
    private UserLite modifiedBy;

    @WhenModified
    @Schema(name = "修改时间")
    private LocalDateTime modifiedDate;

    @WhoCreated
    @Schema(name = "创建者")
    @ManyToOne
    private UserLite createdBy;

    @WhenCreated
    @Schema(name = "创建时间")
    private LocalDateTime createdDate;

    private Long order;
}