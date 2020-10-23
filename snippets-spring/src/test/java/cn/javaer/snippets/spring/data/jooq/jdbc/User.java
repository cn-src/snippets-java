package cn.javaer.snippets.spring.data.jooq.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
class User {
    @Id
    private Long id;

    private String name;

    @CreatedBy
    private Long createdById;

    @CreatedDate
    private LocalDateTime createdDate;
}
