package cn.javaer.snippets.spring.data.jooq.jdbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.jooq.JSONB;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table("users")
class User {
    @Id
    private Long id;

    private String name;

    private JSONB jsonb1;

    @CreatedBy
    private Long createdById;

    @CreatedDate
    private LocalDateTime createdDate;
}
