package cn.javaer.snippets.jooq.codegen.withentity;

import lombok.Value;
import org.jooq.JSONB;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@Value
public class Demo {
    @Id
    Long id;

    String name;

    String[] org;

    JSONB jsonb;

    @CreatedDate
    LocalDateTime createdDate;
}
