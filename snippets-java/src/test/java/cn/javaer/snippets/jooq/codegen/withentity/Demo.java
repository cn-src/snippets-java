package cn.javaer.snippets.jooq.codegen.withentity;

import lombok.Value;
import org.jooq.JSONB;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @author zhangpeng
 */
@Value
public class Demo {
    @Id
    Long id;

    String name;

    JSONB jsonb;

    LocalDateTime createdDate;
}
