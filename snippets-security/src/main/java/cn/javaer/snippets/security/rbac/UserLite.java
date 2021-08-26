package cn.javaer.snippets.security.rbac;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

/**
 * @author cn-src
 */
@Data
@Entity
@Table(name = "users")
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserLite {
    @Id
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    public UserLite() {
    }

    public UserLite(String name) {
        this.name = name;
    }

    public UserLite(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}