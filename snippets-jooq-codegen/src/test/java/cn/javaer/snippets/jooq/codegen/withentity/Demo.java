package cn.javaer.snippets.jooq.codegen.withentity;

import lombok.Value;
import org.jooq.JSONB;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@Value
public class Demo {
    public enum Key {
        DEMO
    }

    @Id
    Long id;
    Key key;
    String name;

    String[] org;

    JSONB jsonb;

    Duration duration;

    @CreatedDate
    LocalDateTime createdDate;
}